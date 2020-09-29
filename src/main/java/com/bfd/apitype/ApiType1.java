package com.bfd.apitype;

import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.bfd.tools.HttpClientHelper.sendGet;
import static com.bfd.tools.Kafkautils.sendMessage;

/**
 * @author everywherewego
 * @date 2020-09-21 23:20
 */
//该类型的api为实时更新的api,3s数据更新一次
//实例api:http://changzhou.czczh.cn/a/api/station/v1/getLatestDataWithSNS?sns=C00E-0005&ak=w6fMRS9IN0hzOHHb
public class ApiType1 {
    private static Logger logger = LoggerFactory.getLogger(ApiType1.class);

    public static String lasttime = "";


    private static void sendhttp(String httptype, String url, String topic) {
        if ("get".equals(httptype.toLowerCase())) {
            String re = sendGet(url, null, null, null).get("responseContext");
            JSONObject jsonObject = JSONObject.parseObject(re);
            //判断重复
            String time = JsonPath.read(re, "$.data[0].time");
            if (!time.equals(lasttime)) {
                lasttime = time;
                sendMessage(topic, jsonObject.toString());
            }
        } else if ("post".equals(httptype.toLowerCase())) {
            System.out.println("发送post请求");
        }
    }


    private static void startSchedule(String httptype, String url, int time, String topic) {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        sendhttp(httptype, url, topic);
                    }
                },
                1,
                time,
                TimeUnit.SECONDS);
        logger.info("程序已经启动");
    }

    public void run(String[] type1Params) {
        startSchedule(type1Params[2], type1Params[1], Integer.parseInt(type1Params[3]), type1Params[8]);

    }

}