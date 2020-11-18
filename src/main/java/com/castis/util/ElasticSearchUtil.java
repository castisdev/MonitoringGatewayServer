package com.castis.util;

import java.util.Date;

import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.PipelineAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;


public class ElasticSearchUtil {

	public ElasticSearchUtil() {

	}

	public static String makeIndexName(String defaultIndex, String startDate, String endDate, String dateFormat, String indexType) throws Exception {
		if( StringUtil.isNull(startDate) || StringUtil.isNull(endDate) )
			return defaultIndex;
		
		Date sDate = DateUtil.getDate(startDate, dateFormat);
		Date eDate = DateUtil.getDate(endDate, dateFormat);
		
		String indexName = "";
		if( "M".equalsIgnoreCase(indexType) ) {
			sDate = DateUtil.getDate(DateUtil.date2String(sDate, "yyyy-MM-dd"), "yyyy-MM");
			eDate = DateUtil.getDate(DateUtil.date2String(eDate, "yyyy-MM-dd"), "yyyy-MM");
			
			String indexNameDateFormat = "yyyy.MM";
			for(Date currDate = sDate; (currDate.before(eDate) || currDate.equals(eDate)); currDate = DateUtil.getAddMonth(currDate, 1)) {
				if( currDate.equals(sDate) == false ) {
					indexName = indexName.concat(",");
				}
				
				String currDateStr = DateUtil.date2String(currDate, indexNameDateFormat);
				indexName = indexName.concat(defaultIndex).concat("-").concat(currDateStr);
			}
		} else {
			sDate = DateUtil.getDate(DateUtil.date2String(sDate, "yyyy-MM-dd"), "yyyy-MM-dd");
			eDate = DateUtil.getDate(DateUtil.date2String(eDate, "yyyy-MM-dd"), "yyyy-MM-dd");
			
			String indexNameDateFormat = "yyyy.MM.dd";
			for(Date currDate = sDate; (currDate.before(eDate) || currDate.equals(eDate)); currDate = DateUtil.getAddDay(currDate, 1)) {
				if( currDate.equals(sDate) == false ) {
					indexName = indexName.concat(",");
				}
				
				String currDateStr = DateUtil.date2String(currDate, indexNameDateFormat);
				indexName = indexName.concat(defaultIndex).concat("-").concat(currDateStr);
			}
		}
			
		return indexName;
	}
	
	public static void addSubAggregationBuilder(SearchSourceBuilder mainAgb, AggregationBuilder parentAgb, AggregationBuilder currAgb) {
		if( parentAgb == null ) {
			mainAgb.aggregation(currAgb);
		} else {
			parentAgb.subAggregation(currAgb);
		}
	}
	
	public static void addSubAggregationBuilder(SearchSourceBuilder mainAgb, AggregationBuilder parentAgb, PipelineAggregationBuilder currAgb) {
		if( parentAgb == null ) {
			mainAgb.aggregation(currAgb);
		} else {
			parentAgb.subAggregation(currAgb);
		}
	}
}
