package com.castis.channel.dto;

import lombok.Data;

@Data
public class E2EViewingTimePerHour {
	private String startDateTime;
	private String endDateTime;
	private Integer allViewingTime;
	
	public E2EViewingTimePerHour() {
		
	}
}
