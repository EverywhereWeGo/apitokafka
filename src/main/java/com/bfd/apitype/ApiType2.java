package com.bfd.apitype;

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
import static com.bfd.tools.StringCompiler.compilerToCode;
import static com.bfd.tools.basic.i_StringUtil.getFirstSubString;

/**
 * @author everywherewego
 * @date 2020-09-21 23:20
 */
public class ApiType2 implements ApiType {
    private static Logger logger = LoggerFactory.getLogger(ApiType2.class);
    public static ConcurrentHashMap<String, Integer> current = new ConcurrentHashMap<>();

    private static void sendhttp(String postmanString, String topic) {
        try {
            Request request = generatorRequest(postmanString);
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            System.out.println(result);
//                sendMessage(topic, jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void startSchedule(String topic, String postmanString, int time, String incrementExpression, String stepSizeExpression, String stepSize,
                                      String loginInfo, String loginMethod) {
        String firstSubString = getFirstSubString(stepSizeExpression, "{", "}");
        current.put(topic, Integer.parseInt(firstSubString));

        ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(2);
        executorService.scheduleAtFixedRate(
                new Runnable() {
                    @Override
                    public void run() {
                        String replace = stepSizeExpression.replace("{" + firstSubString + "}", "{" + (current.get(topic) + Integer.parseInt(stepSize)) + "}")
                                .replace("{", "").replace("}", "");
                        String nextPage = postmanString.replace(incrementExpression, replace);

                        //如果有前置验证则先验证
                        if (null != loginInfo) {
                            Class<?> loginClass = compilerToCode(loginMethod);
                            LoginProcessor loginProcessor = null;
                            try {
                                loginProcessor = (LoginProcessor) loginClass.newInstance();
                            } catch (InstantiationException | IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            nextPage = nextPage.replace(loginInfo, loginProcessor.getLoginInfo());
                        }

                        sendhttp(nextPage, topic);
                        current.put(topic, current.get(topic) + Integer.parseInt(stepSize));
                    }
                },
                1,
                time,
                TimeUnit.SECONDS);
        logger.info("程序已经启动:\n" + postmanString);
    }


    @Override
    public void run(String[] params) {
        startSchedule(params[1], params[2], Integer.parseInt(params[3]), params[4], params[5], params[6], params[7], params[8]);
    }
}