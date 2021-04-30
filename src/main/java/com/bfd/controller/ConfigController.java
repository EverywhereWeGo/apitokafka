package com.bfd.controller;

import com.bfd.common.result.Result;
import com.bfd.domain.dao.ApiConfig;
import com.bfd.mapper.ApiConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

@RequestMapping(value = "/config")
@RestController
public class ConfigController {
    private static Logger logger = LoggerFactory.getLogger(ConfigController.class);
    @Autowired
    private ApiConfigMapper apiConfigMapper;


    @PostMapping(value = "/add")
    public Result add(@RequestBody Map<String, String> map) {
        String address = map.get("address");
        String username = map.get("username");
        String password = map.get("password");

        //检查连接性
        Connection conn;
        try {
            conn = DriverManager.getConnection(address, username, password);
            if (!conn.isValid(100000)) {
                return Result.failed("该地址不能连通");
            }
            conn.close();
        } catch (SQLException e) {
            return Result.failed(e.getMessage());
        }

        //没问题入库
        ApiConfig apiConfig = new ApiConfig();
        apiConfig.setAddress(address);
        apiConfig.setUsername(username);
        apiConfig.setPassword(password);
        apiConfigMapper.insert(apiConfig);

        System.setProperty("url", apiConfig.getAddress());
        System.setProperty("username", apiConfig.getUsername());
        System.setProperty("password", apiConfig.getPassword());
        System.setProperty("driverclass", "com.mysql.cj.jdbc.Driver");
        System.setProperty("poolsnum", "50");

        return Result.succeed("添加成功");
    }
}