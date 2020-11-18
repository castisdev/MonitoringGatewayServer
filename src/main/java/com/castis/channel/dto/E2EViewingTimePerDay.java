package com.castis.channel.dto;

import lombok.Data;

@Data
public class E2EViewingTimePerDay {
	private String startDateTime;
	private String endDateTime;
	private Integer perPersonAvgViewingTime;
	private Integer allViewingTime;
	private Integer allAverageTime;
	
	public E2EViewingTimePerDay() {
		
	}
}
