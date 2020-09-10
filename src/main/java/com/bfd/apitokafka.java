package com.bfd;

import com.alibaba.fastjson.JSONObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static com.bfd.tools.HttpClientHelper.sendGet;
import static com.bfd.tools.ReadExcel.readexcel;
import static com.bfd.tools.ReadExcel.stringtoInt;

/**
 * @author everywherewego
 * @date 2020-09-03 18:42
 */

public class apitokafka {
    private static Logger logger = LoggerFactory.getLogger(apitokafka.class);

    public static void run() {

        int startrow = 1;
        int endrow = 2;
        int startcol = stringtoInt("A");
        int endcol = stringtoInt("D");

        String[][] readexcel = new String[0][];
        try {
            readexcel = readexcel("/Users/everywherewego/Desktop/api接数.xlsx", 0, startrow, endrow, startcol, endcol);
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
        String url;
        String type;
        String time;
        String json;
        for (int i = 1; i <= endrow - startrow; i++) {
            url = readexcel[i][0];
            type = readexcel[i][1];
            if ("get".equals(type.toLowerCase())) {
                String re = sendGet(url, null, null, null).get("responseContext");
                JSONObject jsonObject = JSONObject.parseObject(re);
                System.out.println(jsonObject);

            } else if ("post".equals(type.toLowerCase())) {
                System.out.println("发送post请求");

            }
            System.out.println();
        }
    }

    public static void startSchedule() {
        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(
                apitokafka::run,
                1,
                1,
                TimeUnit.SECONDS);
        logger.info("程序已经启动");
    }

    public static void main(String[] args) {

        startSchedule();
    }
}
