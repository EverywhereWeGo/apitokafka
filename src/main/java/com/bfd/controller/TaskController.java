package com.bfd.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bfd.common.result.Result;
import com.bfd.domain.dao.ApiInfo;
import com.bfd.mapper.ApiInfoMapper;
import com.bfd.service.quartzjob.QuartzJobType1;
import com.bfd.service.quartzjob.QuartzJobType2;
import com.bfd.service.quartzjob.QuartzJobType3;
import com.bfd.tools.QuartzUtils;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.bfd.tools.PostmanParse.generatorRequest;
import static com.bfd.tools.PostmanParse.getUrl;
import static com.bfd.tools.basic.f_SqlUtil.ddlSql;

@RequestMapping(value = "/api")
@RestController
public class TaskController {
    private static Logger logger = LoggerFactory.getLogger(TaskController.class);
    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Autowired
    private Scheduler scheduler;

    @GetMapping(value = "/list")
    public Result list() {
        List<ApiInfo> apiInfos = apiInfoMapper.selectList(null);
        return Result.succeed(apiInfos);
    }

    @PostMapping(value = "/add")
    public Result add(@RequestBody Map<String, String> map) throws SchedulerException {
        //先验证能否访问
        String postmanString = map.get("postmanString");
        try {
            Request request = generatorRequest(postmanString);
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            Response response = client.newCall(request).execute();
            response.close();
        } catch (Exception e) {
            logger.error("", e);
            return Result.failed("请求失败,请检查postman格式" + e);
        }

        //验证topicname是否存在
        String topicName = map.get("topicName");
        QueryWrapper<ApiInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("topic_name", topicName);
        Integer integer = apiInfoMapper.selectCount(queryWrapper);
        if (integer > 0) {
            return Result.failed("topic名称已被占用");
        }

        ApiInfo apiInfo = new ApiInfo();
        apiInfo.setUrl(getUrl(postmanString));
        apiInfo.setType(Integer.parseInt(map.get("apitype")));
        apiInfo.setName(map.get("apiname"));
        apiInfo.setTopicName(map.get("topicName"));
        apiInfo.setOtherparams(JSONObject.toJSONString(map));
        apiInfo.setStatus("r");

        if ("1".equals(map.get("apitype"))) {
            apiInfo.setIntervalTime(map.get("interval"));
            apiInfoMapper.insert(apiInfo);
            QuartzUtils.createJob(scheduler, QuartzJobType1.class, String.valueOf(apiInfo.getId()), "wangchong", String.valueOf(apiInfo.getIntervalTime()), map);
        } else if ("2".equals(map.get("apitype"))) {
            apiInfo.setIntervalTime(map.get("interval"));
            apiInfoMapper.insert(apiInfo);

            QuartzUtils.createJob(scheduler, QuartzJobType2.class, String.valueOf(apiInfo.getId()), "wangchong", String.valueOf(apiInfo.getIntervalTime()), map);
        } else if ("3".equals(map.get("apitype"))) {
            apiInfo.setIntervalTime("-");
            apiInfoMapper.insert(apiInfo);

            QuartzUtils.createJob(scheduler, QuartzJobType3.class, String.valueOf(apiInfo.getId()), "wangchong", QuartzUtils.getCronAfterNow(3), map);

        }
        return Result.succeed("创建成功");
    }

    @GetMapping("/delete/{jobid}")
    public Result<String> delJob(@PathVariable String jobid) throws SchedulerException {
        apiInfoMapper.deleteById(jobid);
        QuartzUtils.deleteJob(scheduler, jobid, "wangchong");
        return Result.succeed("删除成功");
    }

    @GetMapping("/switchstatus/{jobid}")
    public Result<String> pauseJob(@PathVariable String jobid) throws SchedulerException {
        ApiInfo apiInfo = apiInfoMapper.selectById(jobid);
        if (apiInfo.getType() == 3) {
            if (null == apiInfo.getCurrentFlag()) {
                QuartzUtils.refreshJob(scheduler, jobid, "wangchong", QuartzUtils.getCronAfterNow(3));
            }
            return Result.succeed("执行成功");
        }

        if ("r".equals(apiInfo.getStatus())) {
            apiInfo.setStatus("p");
            apiInfoMapper.updateById(apiInfo);
            QuartzUtils.pauseJob(scheduler, jobid, "wangchong");
        } else {
            apiInfo.setStatus("r");
            apiInfoMapper.updateById(apiInfo);
            QuartzUtils.resumeJob(scheduler, jobid, "wangchong");
        }
        return Result.succeed("执行成功");
    }

    @PostMapping("/updata/{jobid}")
    public Result<String> updateJob(@PathVariable String jobid, @RequestBody Map<String, String> map) throws SchedulerException {
        ApiInfo apiInfo = apiInfoMapper.selectById(jobid);
        apiInfo.setStatus("r");

        if (apiInfo.getType() == 3) {
            apiInfo.setIntervalTime("-");
            QuartzUtils.refreshJob(scheduler, jobid, "wangchong", QuartzUtils.getCronAfterNow(3));
        } else {
            apiInfo.setIntervalTime(map.get("interval"));
            QuartzUtils.refreshJob(scheduler, jobid, "wangchong", map.get("interval"));
        }
        apiInfoMapper.updateById(apiInfo);

        return Result.succeed("更新成功");
    }

    @GetMapping("/reset/{jobid}")
    public Result<String> reset(@PathVariable String jobid) throws SchedulerException {
        ApiInfo apiInfo = apiInfoMapper.selectById(jobid);
        //暂停调度
        QuartzUtils.pauseJob(scheduler, jobid, "wangchong");

        //更新状态
        apiInfo.setCurrentFlag(null);
        apiInfo.setStatus("p");
        apiInfoMapper.updateById(apiInfo);

        //删除表
        ddlSql("drop table if exists " + apiInfo.getTopicName());
        return Result.succeed("重置成功");
    }
}