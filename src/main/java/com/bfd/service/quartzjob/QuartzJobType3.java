package com.bfd.service.quartzjob;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bfd.domain.dao.ApiInfo;
import com.bfd.mapper.ApiInfoMapper;
import com.bfd.tools.JsonPathUtil;
import com.bfd.tools.JsonToTable;
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

/**
 * @author everywherewego
 * @date 3/13/21 5:15 PM
 */
@Component
@DisallowConcurrentExecution
public class QuartzJobType3 extends QuartzJobBean {
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


        try {
            Request request = generatorRequest(jobDataMap.getString("postmanString"));
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Response response = client.newCall(request).execute();
            String jsonResult = response.body().string();
            response.close();

            JSONArray jsonArray = JsonPathUtil.parseToJsonArryByJsonPath(jsonResult, jobDataMap.getString("loadfield"));

            JsonToTable.insertDatatoMysql(jsonArray, apiInfo.getTopicName());

            apiInfo.setStatus("p");
            apiInfo.setCurrentFlag("-");
            apiInfoMapper.updateById(apiInfo);

        } catch (
                Exception e) {
            e.printStackTrace();
        }

    }
}
