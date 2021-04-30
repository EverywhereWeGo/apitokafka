package com.bfd.common.config;

import com.bfd.domain.dao.ApiConfig;
import com.bfd.mapper.ApiConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class InitProperties {
    @Autowired
    private ApiConfigMapper apiConfigMapper;

    @Bean
    public void setPro() {
        ApiConfig apiConfig = apiConfigMapper.selectById(1);
        if (apiConfig != null) {
            System.setProperty("url", apiConfig.getAddress());
            System.setProperty("username", apiConfig.getUsername());
            System.setProperty("password", apiConfig.getPassword());
            System.setProperty("driverclass", "com.mysql.cj.jdbc.Driver");
            System.setProperty("poolsnum", "50");
        }
    }
}
