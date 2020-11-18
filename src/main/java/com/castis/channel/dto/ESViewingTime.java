package com.castis.channel.dto;

import java.util.List;

import lombok.Data;

@Data
public class ESViewingTime {
	private String logDateTime;
	private Integer allViewingTime;
	private List<String> profileIds;
	private List<String> channelIds;
}
