package com.castis.config;

import javax.annotation.PostConstruct;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.castis.channel.job.ChannelViewingTimePerDayJob;
import com.castis.channel.job.ChannelViewingTimePerHourJob;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class QuartzConfig {
	@Value("${quartz.channel.viewing_time_day.cron:0 5/60 * * * ?}")
	private String channelViewingTimePerDayCronExpression;
	@Value("${quartz.channel.viewing_time_hour.cron:0 10/60 * * * ?}")
	private String channelViewingTimePerHourCronExpression;
	
	@Autowired
	private Scheduler channelViewingScheduler;
	
	@PostConstruct
	public void init() {
		try {
			JobDetail channelViewingTimePerDayJob = buildChannelViewingTimePerDayJob();
			Trigger channelViewingTimePerDayTrigger = buildChannelViewingTimePerDayJobTrigger();
			
			JobDetail channelViewingTimePerHourJob = buildChannelViewingTimePerHourJob();
			Trigger channelViewingTimePerHourTrigger = buildChannelViewingTimePerHourJobTrigger();
			
			channelViewingScheduler.scheduleJob(channelViewingTimePerDayJob, channelViewingTimePerDayTrigger);
			channelViewingScheduler.scheduleJob(channelViewingTimePerHourJob, channelViewingTimePerHourTrigger);
		} catch(Exception e) {
			log.error("Failed to start channelViewingScheduler, error[{}]", e.getMessage());
		}
	}
	
	private JobDetail buildChannelViewingTimePerDayJob() {
		JobDataMap jobDataMap = new JobDataMap();
//	    jobDataMap.putAll(params);
	    return JobBuilder.newJob(ChannelViewingTimePerDayJob.class)
	    		.withIdentity("ChannelViewingTimePerDayJob").withDescription("ChannelViewingTimePerDayJob")
	    		.usingJobData(jobDataMap)
	    		.build();
	}
	
	private Trigger buildChannelViewingTimePerDayJobTrigger() {
		return TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(channelViewingTimePerDayCronExpression))
				.build();
	}
	
	private JobDetail buildChannelViewingTimePerHourJob() {
		JobDataMap jobDataMap = new JobDataMap();
//	    jobDataMap.putAll(params);
	    return JobBuilder.newJob(ChannelViewingTimePerHourJob.class)
	    		.withIdentity("ChannelViewingTimePerHourJob").withDescription("ChannelViewingTimePerHourJob")
	    		.usingJobData(jobDataMap)
	    		.build();
	}
	
	private Trigger buildChannelViewingTimePerHourJobTrigger() {
		return TriggerBuilder.newTrigger()
				.withSchedule(CronScheduleBuilder.cronSchedule(channelViewingTimePerHourCronExpression))
				.build();
	}
}
