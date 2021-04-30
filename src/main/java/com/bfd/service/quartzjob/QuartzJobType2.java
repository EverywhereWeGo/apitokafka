package com.bfd.service.quartzjob;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bfd.domain.dao.ApiInfo;
import com.bfd.mapper.ApiInfoMapper;
import com.bfd.tools.JsonToTable;
import com.bfd.tools.QuartzUtils;
import com.jayway.jsonpath.JsonPath;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import static com.bfd.tools.PostmanParse.generatorRequest;
import static com.bfd.tools.basic.i_StringUtil.getFirstSubString;

/**
 * @author everywherewego
 * @date 3/13/21 5:15 PM
 */
@Component
@DisallowConcurrentExecution
public class QuartzJobType2 extends QuartzJobBean {
    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Autowired
    private Scheduler scheduler;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_name", jobDataMap.getString("topicName"));
        ApiInfo apiInfo = apiInfoMapper.selectOne(queryWrapper);


        String replacepage = jobDataMap.getString("replacepage");
        String firstpage = getFirstSubString(replacepage, "{", "}");
        String nextpage;
        if (null == apiInfo.getCurrentFlag()) {
            nextpage = replacepage.replace("{" + firstpage + "}", firstpage);
            apiInfo.setCurrentFlag(firstpage);
        } else {
            nextpage = replacepage.replace("{" + firstpage + "}", String.valueOf(Integer.parseInt(apiInfo.getCurrentFlag()) + jobDataMap.getInt("stepsize")));
            apiInfo.setCurrentFlag(String.valueOf(Integer.parseInt(apiInfo.getCurrentFlag()) + jobDataMap.getInt("stepsize")));
        }


        try {
            Request request = generatorRequest(jobDataMap.getString("postmanString").replace(jobDataMap.getString("replacepostman"), nextpage));
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Response response = client.newCall(request).execute();
            String jsonResult = response.body().string();
            response.close();


            Object object = JsonPath.read(jsonResult, jobDataMap.getString("loadfield"));
            JSONArray jsonArray = JSON.parseArray(object.toString());
            System.out.println();


            if (jsonArray.size() > 0) {
                JsonToTable.insertDatatoMysql(jsonArray, apiInfo.getTopicName());
                apiInfoMapper.updateById(apiInfo);
            } else {
                //没数据暂停调度
                QuartzUtils.pauseJob(scheduler, String.valueOf(apiInfo.getId()), "wangchong");
                apiInfo.setStatus("p");
                apiInfo.setCurrentFlag(String.valueOf(Integer.parseInt(apiInfo.getCurrentFlag()) - 1));
                apiInfoMapper.updateById(apiInfo);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
