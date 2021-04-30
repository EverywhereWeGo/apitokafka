package com.bfd.service.quartzjob;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bfd.domain.dao.ApiInfo;
import com.bfd.mapper.ApiInfoMapper;
import com.bfd.tools.JsonToTable;
import com.jayway.jsonpath.JsonPath;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
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
public class QuartzJobType1 extends QuartzJobBean {
    @Autowired
    private ApiInfoMapper apiInfoMapper;

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

            JSONObject jsonObject = JSONObject.parseObject(jsonResult);


            //判断重复
            String incrementFiled = JsonPath.read(jsonObject, jobDataMap.getString("incrementExpression"));
            if (!incrementFiled.equals(apiInfo.getCurrentFlag())) {
                apiInfo.setCurrentFlag(incrementFiled);
                JSONArray loadfield = jsonObject.getJSONArray(jobDataMap.getString("loadfield"));

                if (loadfield.size() > 0) {
                    JsonToTable.insertDatatoMysql(loadfield, apiInfo.getTopicName());
                    apiInfoMapper.updateById(apiInfo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
