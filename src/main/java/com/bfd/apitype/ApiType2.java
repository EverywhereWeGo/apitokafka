package com.bfd.apitype;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.bfd.tools.HttpClientHelper.sendGet;

/**
 * @author everywherewego
 * @date 2020-09-21 23:20
 */
public class ApiType2 {
    private static Logger logger = LoggerFactory.getLogger(ApiType2.class);


    private static void sendhttp(String httptype, String url) {
        if ("get".equals(httptype.toLowerCase())) {
            String re = sendGet(url, null, null, null).get("responseContext");
            JSONObject jsonObject = JSONObject.parseObject(re);
//            logger.info(jsonObject.toString());
            //判断重复
            String time = JsonPath.read(re, "$.data[0].time");
        } else if ("post".equals(httptype.toLowerCase())) {
            System.out.println("发送post请求");

        }


    }

    private static void startSchedule(String httptype, String url, int time) {
        String a = httptype;
        String b = url;
        int c = time;
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        sendhttp(a, b);
                    }
                },
                1,
                c,
                TimeUnit.SECONDS);
        logger.info("程序已经启动");
    }

    public void run(String[] type1Params) {
        startSchedule(type1Params[2], type1Params[1], Integer.parseInt(type1Params[3]));
    }

}