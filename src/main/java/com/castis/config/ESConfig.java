package com.castis.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;

@Configuration
public class ESConfig {
//	@Value("${elasticsearch.host}")
//    private String host;
// 
//    @Value("${elasticsearch.port}")
//    private int port;
// 
//    @Value("${elasticsearch.clustername}")
//    private String clusterName;

	@Value("${elasticsearch.serviceUrl:http://127.0.0.1:9200}")
	private String serviceUrl = "";

	@Value("${elasticsearch.max_connection:10}")
	private int maxTotalConnection = 10;

	@Value("${elasticsearch.conn_timeout:10000}")
	private int connTimeout = 10000;

	@Value("${elasticsearch.read_timeout:10000}")
	private int readTimeout = 10000;
	
	@Value("${elasticsearch.max_connection_idle_time:10000}")
	private int maxConnIdleTime = 10000;
	
//    @Bean
//    public Client client() throws Exception {
////        Settings esSettings = Settings.builder().put("cluster.name", clusterName).build();
//    	Settings esSettings = Settings.builder().put("cluster.name", "Viettel-IBM-VOD-5.6G").build();
//        
//        TransportClient client = new PreBuiltTransportClient(esSettings);
////        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port));
//        client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("172.16.32.101"), 9200));
//        
//        return client;
//    }
// 
//    @Bean
//    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
//        return new ElasticsearchTemplate(client());
//    }

	@Bean
	public JestClient jestClient(){
		JestClientFactory factory = new JestClientFactory();
		
		factory.setHttpClientConfig(
				new HttpClientConfig.Builder(serviceUrl)
					.multiThreaded(true)
					.maxTotalConnection(maxTotalConnection)
					.connTimeout(connTimeout)
					.readTimeout(readTimeout)
					.maxConnectionIdleTime(maxConnIdleTime, TimeUnit.MILLISECONDS)
					.build()
					);
		
		return factory.getObject();
	}
}
