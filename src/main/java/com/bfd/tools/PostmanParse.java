package com.bfd.tools;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.util.HashMap;
import java.util.Map;

import static com.bfd.tools.basic.i_StringUtil.getFirstSubString;

/**
 * @author everywherewego
 * @date 3/2/21 9:18 PM
 */

public class PostmanParse {
    //url根据.url(" 和 ") 获取
    public static String getUrl(String response) {
        String url = getFirstSubString(response, ".url(\"", "\")");
        return url;
    }

    //method 根据.method(" 和 ", 截取
    public static String getMethod(String response) {
        String method = getFirstSubString(response, ".method(\"", "\",");
        return method;
    }


    public static Map<String, String> getHeaders(String response) {
        return getMap(response, "addHeader");
    }

    public static Map<String, String> getAddFormDataPart(String response) {
        return getMap(response, "addFormDataPart");
    }


    public static Map<String, String> getMap(String allString, String matching) {
        HashMap<String, String> match = new HashMap<>();
        int flag = 0;
        while (true) {
            int addHeader = allString.indexOf(matching, flag);
            if (addHeader == -1) {
                break;
            } else {
                String firstSubString = getFirstSubString(allString.substring(flag), matching + "(", ")");
                //keyvalue根据 ", " 分割
                String[] split = firstSubString.split("\", \"");
                match.put(split[0].substring(1), split[1].substring(0, split[1].length() - 1));
                flag = addHeader + 1;
            }
        }
        return match;
    }


    public static String getMediaType(String postmanString) {
        String mediType = getFirstSubString(postmanString, "MediaType mediaType = MediaType.parse(\"", "\");");
        return mediType;

    }

    public static Request generatorPost(String url, Map<String, String> header, String postmanString) {
        Request.Builder builder = new Request.Builder();


        MediaType mediaType = MediaType.parse(getMediaType(postmanString));
        RequestBody body;

        if (postmanString.contains("RequestBody body = RequestBody.create(")) {
            String context = getFirstSubString(postmanString, "RequestBody body = RequestBody.create(mediaType, \"", "\");").replace("\\\"", "\"");
            body = RequestBody.create(mediaType, context);
        } else {
            MultipartBody.Builder bodybuilder = new MultipartBody.Builder();
            bodybuilder.setType(MultipartBody.FORM);
            Map<String, String> addFormDataPart = getAddFormDataPart(postmanString);
            for (Map.Entry<String, String> e : addFormDataPart.entrySet()) {
                bodybuilder.addFormDataPart(e.getKey(), e.getValue());
            }
            body = bodybuilder.build();
        }

        builder.url(url).method("POST", body);
        for (Map.Entry<String, String> e : header.entrySet()) {
            builder.addHeader(e.getKey(), e.getValue());
        }
        Request request = builder.build();

        return request;

    }


    public static Request generatorGet(String url, Map<String, String> header) {
        Request.Builder builder = new Request.Builder();
        builder.url(url).method("GET", null);

        for (Map.Entry<String, String> e : header.entrySet()) {
            builder.addHeader(e.getKey(), e.getValue());
        }
        Request build = builder.build();

        return build;

    }

    public static Request generatorRequest(String postmanString) {
        Request request = null;

        String url = getUrl(postmanString);
        String method = getMethod(postmanString);
        Map<String, String> headers = getHeaders(postmanString);
        if ("GET".equals(method)) {
            request = generatorGet(url, headers);
        } else if ("POST".equals(method)) {
            request = generatorPost(url, headers, postmanString);
        }

        return request;
    }
}
