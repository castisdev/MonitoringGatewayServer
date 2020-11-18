package com.castis.channel.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.castis.channel.dto.E2EViewingTimePerDay;
import com.castis.channel.dto.E2EViewingTimePerHour;
import com.castis.channel.dto.ESViewingTime;
import com.castis.channel.service.ChannelViewingService;
import com.castis.exception.http.InternalServerException;
import com.castis.exception.http.InvalidParamException;
import com.castis.util.ParameterCheckUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ChannelViewingController {
	@Autowired
	private ChannelViewingService service;
	
	@GetMapping(value = "/cdn/channel/viewingTime/day")
	public @ResponseBody E2EViewingTimePerDay getViewingTimePerDay(
			@RequestParam(value = "startDateTime") String startDateTime
			, @RequestParam(value = "endDateTime") String endDateTime) {
		try {
			if( ParameterCheckUtil.checkDate(startDateTime, "yyyyMMddHHmmss") == false ) {
				throw new InvalidParamException();
			}
			if( ParameterCheckUtil.checkDate(endDateTime, "yyyyMMddHHmmss") == false ) {
				throw new InvalidParamException();
			}			
			
			List<ESViewingTime> esViewingTimeList = service.getViewingTimeList(startDateTime, endDateTime);
			
			E2EViewingTimePerDay result = service.convertE2EViewingTimePerDay(startDateTime, endDateTime, esViewingTimeList);
			
			return result;
		} catch(InvalidParamException e) {
			throw e;
		} catch(Exception e) {
			log.error("Failed to get viewingTimeListPerDay, error[{}]", e.getMessage());
			throw new InternalServerException();
		}
	}
	
	@GetMapping(value = "/cdn/channel/viewingTime/hour")
	public @ResponseBody E2EViewingTimePerHour getViewingTimePerHour(
			@RequestParam(name = "startDateTime") String startDateTime
			, @RequestParam(name = "endDateTime") String endDateTime) {
		try {
			if( ParameterCheckUtil.checkDate(startDateTime, "yyyyMMddHHmmss") == false ) {
				throw new InvalidParamException();
			}
			if( ParameterCheckUtil.checkDate(endDateTime, "yyyyMMddHHmmss") == false ) {
				throw new InvalidParamException();
			}
			
			List<ESViewingTime> esViewingTimeList = service.getViewingTimeList(startDateTime, endDateTime);
			
			E2EViewingTimePerHour result = service.convertE2EViewingTimePerHour(startDateTime, endDateTime, esViewingTimeList);
			
			return result;
		} catch(InvalidParamException e) {
			throw e;
		} catch(Exception e) {
			log.error("Failed to get viewingTimeListPerHour, error[{}]", e.getMessage());
			throw new InternalServerException();
		}
	}
}
