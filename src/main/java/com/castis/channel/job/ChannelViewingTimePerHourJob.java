package com.castis.channel.job;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.castis.channel.dto.E2EViewingTimePerHour;
import com.castis.channel.dto.ESViewingTime;
import com.castis.channel.dto.ES.ESCommonResponse;
import com.castis.channel.service.ChannelViewingService;
import com.castis.util.Constants;
import com.castis.util.DateUtil;
import com.castis.util.HttpCommunicationUtil;
import com.castis.util.StringUtil;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ChannelViewingTimePerHourJob extends QuartzJobBean {	
	@Value(value = "${system.header.e2e.transation.name:}")
	private String transactionName;
	@Value(value = "${system.auth.e2e.admin:}")
	private String e2eAdmin;
	@Value(value = "${system.auth.e2e.password:}")
	private String e2ePassword;
	@Value(value = "${system.api.e2e.channel.viewing_time_hour.url:}")
	private String viewingTimmeHourUrl;
	
	@Autowired
	private ChannelViewingService service;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		try {
			log.info("ChannelViewingTimePerHourJob Start");
		
			sendE2EViewingTimePerHour();
		} catch(Exception e) {
			log.error("Failed to excute ChannelViewingService, error[{}]", e.getMessage());
		} finally {
			log.info("ChannelViewingTimePerHourJob End");
		}
	}
	
	private void sendE2EViewingTimePerHour() {
		if( StringUtil.isNull(viewingTimmeHourUrl) ) {
			log.warn("Url is empty");
			return;
		}
		
		List<ESViewingTime> channelViewingList = null;
		String startDateTime = "", endDateTime = "";
		try {
			Date now = new Date();
			startDateTime = DateUtil.date2String(DateUtil.getAddHour(now, -1), "yyyyMMddHH0000");
			endDateTime = DateUtil.date2String(now, "yyyyMMddHH0000");
			
			channelViewingList = service.getViewingTimeList(startDateTime, endDateTime);
		} catch(Exception e) {
			log.error("Failed to getViewingTimeList, error[{}]", e.getMessage());
			return;
		}
		
		if( channelViewingList == null || channelViewingList.isEmpty() ) {
			log.warn("Empty channelViewingTimeList");
			return;
		}
		
		// convert es -> e2e model
		E2EViewingTimePerHour e2eViewingTimePerHour = null;
		try {
			e2eViewingTimePerHour = service.convertE2EViewingTimePerHour(startDateTime, endDateTime, channelViewingList);
		} catch(Exception e) {
			log.error("Failed to convert data, error{}", e.getMessage());
			return;
		}
		
		if( e2eViewingTimePerHour == null ) {
			log.error("Empty data");
			return;
		}
		
		// request to e2e
		try {
			HttpMethod httpMethod = HttpMethod.POST;
			String requestBody = (new Gson()).toJson(e2eViewingTimePerHour);
			UUID transactionId = UUID.randomUUID();
			
			ResponseEntity<ESCommonResponse> response = HttpCommunicationUtil.<ESCommonResponse>communicate(httpMethod
					, viewingTimmeHourUrl, requestBody, transactionName, transactionId.toString(), e2eAdmin, e2ePassword, ESCommonResponse.class);
			
			if( response != null ) {
				int resultCode = response.getStatusCode().value();
				ESCommonResponse responseBody = response.getBody();
				
				if( resultCode == Constants.ResponseStatus.SUCCESS.getStatusCode() 
						&& Constants.ReturnCode.SUCCESS.equalsIgnoreCase(responseBody.getReturnCode())) {
					log.info("Success to request, transactionId[{}])", transactionId.toString());
				} else {
					log.error("Failed to request, transactionId[{}], status[{}], returnCode[{}], error[{}])"
							, transactionId.toString(), resultCode, responseBody.getReturnCode(), responseBody.getErrorString());
				}
			} else {
				log.error("Failted to communicate to E2E system, error[Emtpry response]");
				return;
			}
		} catch(Exception e) {
			log.error("Failted to communicate to E2E system, error[{}]", e.getMessage());
			return;
		}
	}
}
