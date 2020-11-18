package com.castis.channel.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.castis.channel.dto.E2EViewingTimePerDay;
import com.castis.channel.dto.E2EViewingTimePerHour;
import com.castis.channel.dto.ESViewingTime;
import com.castis.common.dao.ElasticSearchDao;
import com.castis.util.Constants;
import com.castis.util.ElasticSearchUtil;

import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import io.searchbox.core.SearchResult.Hit;
import io.searchbox.params.Parameters;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ChannelViewingService {
	@Autowired
	private ElasticSearchDao esDao;
	
	private SearchResult excute(SearchSourceBuilder searchSourceBuilder, String indexName) throws Exception {
		Search search  = new Search.Builder(searchSourceBuilder.toString()).addIndex(indexName)
				.setParameter(Parameters.IGNORE_UNAVAILABLE, true)
				.setParameter(Parameters.ALLOW_NO_INDICES, true)
				.build();

		try {
			SearchResult result = esDao.execute(search);
			return result;
		} catch (Exception e) {
			log.error("Failed to excute query, error[{}]", e.getMessage());
			throw e;
		}
	}
	
	public List<ESViewingTime> getViewingTimeList(String startDateTime, String endDateTime) throws Exception {
		String dateFormat = "yyyyMMddHHmmss";
		///--- query ---
		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
		boolQuery.filter(QueryBuilders.rangeQuery("logDateTime").format(dateFormat).gte(startDateTime).lt(endDateTime));
		///
		
		///--- aggregations ---
		
		///
		
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		
		searchSourceBuilder.size(Constants.ES.MaxRowSize);
		searchSourceBuilder.query(boolQuery);
//		searchSourceBuilder.aggregation(agb);
		
		String indexName = ElasticSearchUtil.makeIndexName("channel_viewingtime", startDateTime, endDateTime, dateFormat, Constants.ES.IndexType.MONTH);
		
		log.debug(indexName);
		log.debug(searchSourceBuilder.toString());
		
		try {
			SearchResult searchResult = this.excute(searchSourceBuilder, indexName);
			
			if( searchResult.isSucceeded() ) {
				List<ESViewingTime> esViewingTime = new ArrayList<ESViewingTime>();
				
				// parsing
				List<Hit<ESViewingTime, Void>> hits = searchResult.getHits(ESViewingTime.class);
				for (Hit<ESViewingTime, Void> hit : hits) {
					esViewingTime.add(hit.source);
				}				
				
				return esViewingTime;
			} else {
				log.error("Failed to exceut query, error[{}]", searchResult.getErrorMessage());
				return null;
			}
		} catch(Exception e) {
			log.error("Failed to getViewingTime, error[{}]", e.getMessage());
			return null;
		}
	}
	
	public E2EViewingTimePerDay convertE2EViewingTimePerDay(String startDateTime, String endDateTime, List<ESViewingTime> esViewingTimeList) {
		if( esViewingTimeList == null || esViewingTimeList.isEmpty() )
			return null;
		
		Set<String> profileIdSet = new HashSet<String>();
		Set<String> channelIdSet = new HashSet<String>();
		Integer allViewingTime = 0;
		for(ESViewingTime esInfo : esViewingTimeList) {
			if( esInfo.getProfileIds() != null )
				profileIdSet.addAll(esInfo.getProfileIds());
			if( esInfo.getChannelIds() != null )
				channelIdSet.addAll(esInfo.getChannelIds());
			allViewingTime += esInfo.getAllViewingTime();
		}
		
		E2EViewingTimePerDay e2eViewingTime = new E2EViewingTimePerDay();
		
		e2eViewingTime.setStartDateTime(startDateTime);
		e2eViewingTime.setEndDateTime(endDateTime);
		if( profileIdSet.isEmpty() == false ) {
			// 오차를 줄이기 위해서 allViewingTime과 따로 계산
			e2eViewingTime.setPerPersonAvgViewingTime(allViewingTime/profileIdSet.size() /60/60); // MA에서 넘겨준 데이터 단위가 초인 경우
		}
		if( channelIdSet.isEmpty() == false ) {
			// 오차를 줄이기 위해서 allViewingTime과 따로 계산
			e2eViewingTime.setAllAverageTime(allViewingTime/channelIdSet.size() /60/60); // MA에서 넘겨준 데이터 단위가 초인 경우
		}
		e2eViewingTime.setAllViewingTime(allViewingTime /60/60); // MA에서 넘겨준 데이터 단위가 초인 경우
		
		return e2eViewingTime;
	}
	
	public E2EViewingTimePerHour convertE2EViewingTimePerHour(String startDateTime, String endDateTime, List<ESViewingTime> esViewingTimeList) {
		if( esViewingTimeList == null || esViewingTimeList.isEmpty() )
			return null;
		
		Integer allViewingTime = 0;
		for(ESViewingTime esInfo : esViewingTimeList) {
			allViewingTime += esInfo.getAllViewingTime();
		}
		
		E2EViewingTimePerHour e2eViewingTime = new E2EViewingTimePerHour();
		
		e2eViewingTime.setStartDateTime(startDateTime);
		e2eViewingTime.setEndDateTime(endDateTime);
		e2eViewingTime.setAllViewingTime(allViewingTime /60/60); // MA에서 넘겨준 데이터 단위가 초인 경우
		
		return e2eViewingTime;
	}
}
