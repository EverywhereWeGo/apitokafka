package com.bfd.tools;

import org.quartz.*;

import java.util.Map;

/**
 * @author everywherewego
 * @date 3/15/21 3:12 PM
 */

public class QuartzUtils {
    //如果inteverTIme 包含空格的话就是cron表达式,否则就是简单表达式
    public static void createJob(Scheduler scheduler, Class<? extends Job> jobClass, String jobName, String jobGroup, String inteverTIme, Map<String, String> param) throws SchedulerException {
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .build();
        if (param != null && !param.isEmpty()) {
            param.forEach((key, value) -> jobDetail.getJobDataMap().put(key, value));
        }

        if (inteverTIme.contains(" ")) {
            scheduler.scheduleJob(jobDetail, buildCronTrigger(jobName, jobGroup, inteverTIme));
        } else {
            scheduler.scheduleJob(jobDetail, buildSimpleTrigger(jobName, jobGroup, Integer.valueOf(inteverTIme)));
        }
    }

    public static void refreshJob(Scheduler scheduler, String jobName, String jobGroup, String inteverTIme) throws SchedulerException {
        TriggerKey triggerKey = TriggerKey.triggerKey(jobName, jobGroup);
        if (inteverTIme.contains(" ")) {
            scheduler.rescheduleJob(triggerKey, buildCronTrigger(jobName, jobGroup, inteverTIme));
        } else {
            scheduler.rescheduleJob(triggerKey, buildSimpleTrigger(jobName, jobGroup, Integer.valueOf(inteverTIme)));
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
        CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cron).withMisfireHandlingInstructionDoNothing();
        return TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(scheduleBuilder)
                .build();
    }

    private static Trigger buildSimpleTrigger(String jobName, String jobGroup, int second) {
        SimpleScheduleBuilder simpleScheduleBuilder = SimpleScheduleBuilder.repeatSecondlyForever(second).withMisfireHandlingInstructionNextWithRemainingCount();
        return TriggerBuilder.newTrigger()
                .withIdentity(jobName, jobGroup)
                .withSchedule(simpleScheduleBuilder)
                .build();
    }

}