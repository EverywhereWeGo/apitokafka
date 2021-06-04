package com.bfd.tools;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static com.bfd.tools.basic.f_SqlUtil.*;

/**
 * @author everywherewego
 * @date 2/22/21 5:20 PM
 */
public class JsonToTable {
    public static Map<String, LinkedList<String>> tableFields = new ConcurrentHashMap<>();
    private static final String SYSTEMID = "sysid";

    private static void tableCheck(String table) {
        ddlSql("create table if not exists " + table + "(`" + SYSTEMID + "` int(255) NOT NULL AUTO_INCREMENT COMMENT '自增id,系统id非业务id,请勿使用',PRIMARY KEY (`" + SYSTEMID + "`))");
    }

    //获取当前表中的字段，排除SYSTEMID
    private static void getTableFieldsByConnection(String tableName) {
        JSONArray tables = querySql("desc " + tableName);

        LinkedList<String> fields = new LinkedList<>();
        for (Object ob : tables) {
            JSONObject jsonObject1 = (JSONObject) ob;
            String b = jsonObject1.getString("Field");
            if (SYSTEMID.equals(b)) {
                continue;
            }
            fields.add(b);
        }
        tableFields.put(tableName, fields);
    }


    //获取新增的字段
    private static Set<String> getNewFields(JSONObject jsonObject, String tabaleName) {
        JSONObject clone = (JSONObject) jsonObject.clone();
        Set<String> keys = clone.keySet();

        LinkedList<String> linkedList = tableFields.get(tabaleName);
        //把已存在的移除掉
        keys.removeAll(linkedList);
        return keys;
    }

    private static void alterTable(String tableName, Set<String> newFields) {
        String modelSql = "ALTER TABLE {0} ADD {1} LONGTEXT";
        for (String e : newFields) {
            String replace = modelSql.replace("{0}", tableName).replace("{1}", e);
            ddlSql(replace);
            tableFields.get(tableName).add(e);
        }
    }

    private static void insertData(String tableName, JSONObject jsonObject) {
        String modelsql = "insert into " + tableName + " ({1}) values ({2}) ";
        StringBuffer fi = new StringBuffer();
        StringBuffer va = new StringBuffer();
        for (String field : jsonObject.keySet()) {
            fi.append(field).append(",");
            va.append("?,");
        }
        fi.deleteCharAt(fi.length() - 1);
        va.deleteCharAt(va.length() - 1);
        String sql = modelsql.replace("{1}", fi).replace("{2}", va);
        insertSql(sql, jsonObject);
    }

    public static void insertDatatoMysql(JSONArray jsonArray, String tableName) {
        //检查表是否存在
        tableCheck(tableName);
        //获取表字段
        getTableFieldsByConnection(tableName);
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            Set<String> newFields = getNewFields(jsonObject, tableName);
            alterTable(tableName, newFields);

            insertData(tableName, jsonObject);
        }

    }
}
