package com.castis.common.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

@Component
public class ElasticSearchDao {
	@Autowired
	private JestClient jestClient;
	
	public SearchResult execute(Search action) throws Exception {
		SearchResult result = jestClient.execute(action);
		
		return result;
	}
}
