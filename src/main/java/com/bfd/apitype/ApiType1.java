package com.bfd.apitype;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.bfd.tools.PostmanParse.generatorRequest;


/**
 * @author everywherewego
 * @date 2020-09-21 23:20
 */
//该类型的api为实时更新的api,3s数据更新一次
//实例api:http://changzhou.czczh.cn/a/api/station/v1/getLatestDataWithSNS?sns=C00E-0005&ak=w6fMRS9IN0hzOHHb
public class ApiType1 implements ApiType {
    private static Logger logger = LoggerFactory.getLogger(ApiType1.class);

    public static ConcurrentHashMap<String, String> incrementValue = new ConcurrentHashMap<>();


    private static void sendhttp(String postmanString, String topic, String incrementExpression) {
        try {
            Request request = generatorRequest(postmanString);
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Response response = client.newCall(request).execute();
            String jsonResult = response.body().string();

            JSONObject jsonObject = JSONObject.parseObject(jsonResult);

            //判断重复
            String incrementFiled = JsonPath.read(jsonObject, incrementExpression);

            if (!incrementFiled.equals(incrementValue.get(topic))) {
                incrementValue.put(topic, incrementFiled);

                System.out.println(jsonObject);
//                sendMessage(topic, jsonObject.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private static void startSchedule(String topic, String postmanString, int time, String incrementExpression) {
        incrementValue.put(topic, "");

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        sendhttp(postmanString, topic, incrementExpression);
                    }
                },
                1,
                time,
                TimeUnit.SECONDS);
        logger.info("程序已经启动:\n" + postmanString);
    }

    @Override
    public void run(String[] params) {
        startSchedule(params[1], params[2], Integer.parseInt(params[3]), params[4]);
    }
}