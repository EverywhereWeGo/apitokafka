package com.bfd.tools;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangchong
 */
public class HttpClientHelper {
    public static HttpClient getHttpClient() {
        HttpClient httpClient = new HttpClient();
        // 设置httpClient连接主机服务器超时时间：15000毫秒
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(100000);
        return httpClient;
    }

    public static Map<String, String> sendPost(String url, Map<String, String> requestHeaders, Map<String, String> params, String bdoy) {
        return sendPost(url, requestHeaders, params, bdoy, null);

    }

    public static Map<String, String> sendPost(String url, Map<String, String> requestHeaders, Map<String, String> params, String bdoy, Map<String, String> config) {
        Map<String, String> responseMap = new HashMap<String, String>(16);

        // 创建httpClient实例对象
        HttpClient httpClient = getHttpClient();
        // 创建post请求方法实例对象
        PostMethod postMethod = new PostMethod(url);
        // 设置post请求超时时间
        postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 100000);
        //设置请求头
        if (null != requestHeaders) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                postMethod.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        //设置参数
        if (null != params) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                postMethod.setParameter(entry.getKey(), entry.getValue());
            }
        }
        //设置requestbody
        if (null != bdoy) {
            postMethod.setRequestBody(bdoy);
        }

        String encodeType = "UTF-8";
        if (null != config) {
            encodeType = config.get("encodeType");
        }

        String result = "";
        try {
            httpClient.executeMethod(postMethod);

            StringBuilder cookiestr = new StringBuilder();
            Cookie[] cookies = httpClient.getState().getCookies();
            if (cookies.length != 0) {
                for (Cookie cookie : cookies) {
                    cookiestr.append(cookie.toString()).append(";");
                }
                cookiestr.deleteCharAt(cookiestr.lastIndexOf(";"));
            }


            BufferedReader reader = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(), encodeType));
            StringBuffer stringBuffer = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();
            responseMap.put("responseCookie", cookiestr.toString());
            responseMap.put("responseContext", result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        postMethod.releaseConnection();
        return responseMap;
    }


    public static Map<String, String> sendGet(String url, Map<String, String> requestHeaders, Map<String, String> params, Map<String, String> config) {
        Map<String, String> responseMap = new HashMap<String, String>(16);
        // 创建httpClient实例对象
        HttpClient httpClient = getHttpClient();
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(url);
        if (null != requestHeaders) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                getMethod.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        //设置参数
        if (null != params) {
            HttpMethodParams pa = new HttpMethodParams();
            for (Map.Entry<String, String> entry : params.entrySet()) {
                pa.setParameter(entry.getKey(), entry.getValue());
            }
            getMethod.setParams(pa);
        }
        try {
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.executeMethod(getMethod);

            StringBuilder cookiestr = new StringBuilder();
            Cookie[] cookies = httpClient.getState().getCookies();
            if (cookies.length != 0) {
                for (Cookie cookie : cookies) {
                    cookiestr.append(cookie.toString()).append(";");
                }
                cookiestr.deleteCharAt(cookiestr.lastIndexOf(";"));
            }

            String encodeType = "UTF-8";
            if (null != config) {
                encodeType = config.get("encodeType");
            }

            String result;
            BufferedReader reader = new BufferedReader(new InputStreamReader(getMethod.getResponseBodyAsStream(), encodeType));
            StringBuffer stringBuffer = new StringBuffer();
            String str;
            while ((str = reader.readLine()) != null) {
                stringBuffer.append(str);
            }
            result = stringBuffer.toString();


            responseMap.put("responseCookie", cookiestr.toString());
            responseMap.put("responseContext", result);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        getMethod.releaseConnection();
        return responseMap;
    }

    /**
     * @param url
     * @param requestHeaders
     * @param path
     * @return //发送get请求获取图片
     */
    public static Map<String, String> sendGetToGetPicture(String url, Map<String, String> requestHeaders, String path) {
        Map<String, String> responseMap = new HashMap<String, String>(16);
        // 创建httpClient实例对象
        HttpClient httpClient = getHttpClient();
        // 创建GET请求方法实例对象
        GetMethod getMethod = new GetMethod(url);
        if (null != requestHeaders) {
            for (Map.Entry<String, String> entry : requestHeaders.entrySet()) {
                getMethod.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            httpClient.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            httpClient.executeMethod(getMethod);

            File storeFile = new File(path);
            FileOutputStream output = new FileOutputStream(storeFile);
            //得到网络资源的字节数组,并写入文件
            output.write(getMethod.getResponseBody());
            output.close();

            StringBuilder cookiestr = new StringBuilder();
            Cookie[] cookies = httpClient.getState().getCookies();
            if (cookies.length != 0) {
                for (Cookie cookie : cookies) {
                    cookiestr.append(cookie.toString()).append(";");
                }
                cookiestr.deleteCharAt(cookiestr.lastIndexOf(";"));
            }

            String result = getMethod.getResponseBodyAsString();
            responseMap.put("responseCookie", cookiestr.toString());
            responseMap.put("responseContext", result);
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }

        getMethod.releaseConnection();

        return responseMap;
    }


    public static String paramsEncdoe(Map<String, String> params) {
        String encode = params.toString().replace(", ", "&");
        String e = encode.substring(1, encode.length() - 1);
        return geturlencoderst(e);
    }

    public static String geturlencoderst(String string) {
        StringBuilder resulturl = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            char charAt = string.charAt(i);
            //只对汉字处理
            if (isChineseChar(charAt)) {
                String encode = null;
                try {
                    encode = URLEncoder.encode(charAt + "", "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                resulturl.append(encode);
            } else {
                resulturl.append(charAt);
            }
        }
        return String.valueOf(resulturl);
    }

    public static boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }


}
