package com.bfd.tools;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jayway.jsonpath.JsonPath;

import static com.bfd.tools.JsonToTable.insertDatatoMysql;

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

    public static void main(String[] args) {
        System.setProperty("url", "jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&failOverReadOnly=false");
        System.setProperty("username", "root");
        System.setProperty("password", "1122334.");
        System.setProperty("driverclass", "com.mysql.cj.jdbc.Driver");
        System.setProperty("poolsnum", "50");

        String a = "{\n" +
                "    \"code\": 200,\n" +
                "    \"data\": \"{\\\"d\\\":[{\\\"vno\\\":\\\"辽A0007X\\\",\\\"owner\\\":\\\"沈阳天鼎旅游汽车客运有限公司\\\",\\\"manage\\\":\\\"沈阳市交通局\\\",\\\"contacter\\\":\\\"\\\",\\\"phone\\\":\\\"手机查车号码\\\",\\\"type\\\":\\\"大型客车\\\",\\\"color\\\":\\\"黄色\\\",\\\"trade\\\":\\\"班车客运\\\"},,{\\\"vno\\\":\\\"辽AZ909K\\\",\\\"owner\\\":\\\"沈阳兴运危险货物运输有限责任公司\\\",\\\"manage\\\":\\\"沈阳市交通局\\\",\\\"contacter\\\":\\\"王朝烨\\\",\\\"phone\\\":\\\"18809808860\\\",\\\"type\\\":\\\"危险品运输车\\\",\\\"color\\\":\\\"蓝色\\\",\\\"trade\\\":\\\"危险品运输\\\"}],\\\"count\\\":9136,\\\"errcode\\\":0}\",\n" +
                "    \"message\": \"\"\n" +
                "}";

        JSONObject jsonObject = JSON.parseObject(a);
        System.out.println(jsonObject);


        JSONArray jsonArray = parseToJsonArryByJsonPath(a, "$");
        System.out.println(jsonArray);

        insertDatatoMysql(jsonArray,"ttt");
    }

}
