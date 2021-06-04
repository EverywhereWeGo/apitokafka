package com.bfd.tools;

import org.quartz.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author everywherewego
 * @date 3/15/21 3:12 PM
 */

public class QuartzUtils {
    //如果inteverTime 包含空格的话就是cron表达式,否则就是简单表达式
    public static void createJob(Scheduler scheduler, Class<? extends Job> jobClass, String jobName, String jobGroup, String inteverTime, Map<String, String> param) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                //该参数指定当triger触发完的时候，是否还保存该jobdetail
                .storeDurably(true)
                .build();
        if (param != null && !param.isEmpty()) {
            param.forEach((key, value) -> jobDetail.getJobDataMap().put(key, value));
        }

        if (inteverTime.contains(" ")) {
            scheduler.scheduleJob(jobDetail, buildCronTrigger(jobName, jobGroup, inteverTime));
        } else {
            scheduler.scheduleJob(jobDetail, buildSimpleTrigger(jobName, jobGroup, Integer.valueOf(inteverTime)));
        }
    }

    public static void refreshJob(Scheduler scheduler, String jobName, String jobGroup, String inteverTime) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        Trigger oldtrigger = scheduler.getTrigger(triggerKey);

        Trigger trigger;
        if (inteverTime.contains(" ")) {
            trigger = buildCronTrigger(jobName, jobGroup, inteverTime);
        } else {
            trigger = buildSimpleTrigger(jobName, jobGroup, Integer.valueOf(inteverTime));
        }

        //如果旧的trigger不存在，删除原来的job，新建一个，否则替换triger
        if (null == oldtrigger) {
            JobDetail jobDetail = scheduler.getJobDetail(JobKey.jobKey(jobName, jobGroup));
            scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
            scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
            scheduler.scheduleJob(jobDetail, trigger);
        } else {
            scheduler.rescheduleJob(triggerKey, trigger);
        }
    }


    public static void resumeJob(Scheduler scheduler, String jobName, String jobGroup) throws SchedulerException {
        scheduler.resumeJob(JobKey.jobKey(jobName, jobGroup));
    }

    public static void pauseJob(Scheduler scheduler, String jobName, String jobGroup) throws SchedulerException {
        scheduler.pauseJob(JobKey.jobKey(jobName, jobGroup));
    }

    public static void deleteJob(Scheduler scheduler, String jobName, String jobGroup) throws SchedulerException {
        scheduler.unscheduleJob(TriggerKey.triggerKey(jobName, jobGroup));
        scheduler.deleteJob(JobKey.jobKey(jobName, jobGroup));
    }

    private static Trigger buildCronTrigger(String jobName, String jobGroup, String cron) {
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron);
        CronTrigger cronTrigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(scheduleBuilder).forJob(jobName, jobGroup)
                .build();

        return cronTrigger;
    }

    private static Trigger buildSimpleTrigger(String jobName, String jobGroup, int second) {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(second).withMisfireHandlingInstructionNextWithRemainingCount();
        SimpleTrigger simpleTrigger = TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(simpleScheduleBuilder)
                .build();
        return simpleTrigger;
    }


    public static String getCronAfterNow(int second) {
        String formatTimeStr = null;
        String dateFormat = "ss mm HH dd MM ? yyyy";
        Date date = new Date(System.currentTimeMillis() + second * 1000);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }

        return formatTimeStr;
    }

    public static void main(String[] args) {
        System.out.println(getCronAfterNow(100));

    }

}