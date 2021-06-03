package com.bfd.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;

/**
 * @author everywherewego
 * @date 6/3/21 10:16 PM
 */

public class JsonPathUtil {
    public static JSONArray parseToJsonArryByJsonPath(String json, String JsonExpression) {
        JSONArray jsonArray = new JSONArray();
        Object object = JsonPath.read(json, JsonExpression);

        if (object instanceof net.minidev.json.JSONArray) {
            jsonArray = JSON.parseArray(object.toString());
        } else if (object instanceof java.util.LinkedHashMap) {
            JSONObject fromHashMap = (JSONObject) JSON.toJSON(object);
            jsonArray.add(fromHashMap);
        }
        return jsonArray;
    }

}
