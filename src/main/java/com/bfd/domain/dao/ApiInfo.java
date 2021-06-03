package com.bfd.domain.dao;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

/**
 * @author everywherewego
 * @date 3/10/21 12:03 PM
 */

public class ApiInfo {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String url;
    private String name;
    private Integer type;
    private String intervalTime;
    private String topicName;
    private String otherparams;
    private String status;
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String currentFlag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(String intervalTime) {
        this.intervalTime = intervalTime;
    }

    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }

    public String getOtherparams() {
        return otherparams;
    }

    public void setOtherparams(String otherparams) {
        this.otherparams = otherparams;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCurrentFlag() {
        return currentFlag;
    }

    public void setCurrentFlag(String currentFlag) {
        this.currentFlag = currentFlag;
    }

    @Override
    public String toString() {
        return "ApiInfo{" +
                "id=" + id +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", intervalTime=" + intervalTime +
                ", topicName='" + topicName + '\'' +
                ", otherparams='" + otherparams + '\'' +
                ", status='" + status + '\'' +
                ", currentFlag='" + currentFlag + '\'' +
                '}';
    }
}
