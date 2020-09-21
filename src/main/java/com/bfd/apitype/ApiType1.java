package com.bfd.apitype;

import com.alibaba.fastjson.JSONObject;
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


public class ApiType1 {
    private static Logger logger = LoggerFactory.getLogger(ApiType1.class);

    private String httptype;
    private String url;

    public ApiType1(String httptype, String url) {
        this.httptype = httptype;
        this.url = url;
    }

    private static void sendhttp(String httptype, String url) {
        if ("get".equals(httptype.toLowerCase())) {
            String re = sendGet(url, null, null, null).get("responseContext");
            JSONObject jsonObject = JSONObject.parseObject(re);
            logger.info(jsonObject.toString());

        } else if ("post".equals(httptype.toLowerCase())) {
            System.out.println("发送post请求");

        }

    }

    private static void startSchedule(String httptype, String url) {
        String a = httptype;
        String b = url;
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        sendhttp(a, b);
                    }
                },
                1,
                1,
                TimeUnit.SECONDS);
        logger.info("程序已经启动");
    }

    public void run() {
        startSchedule(httptype, url);

    }

}