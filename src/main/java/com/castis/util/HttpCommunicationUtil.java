package com.castis.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpCommunicationUtil {

	public static <T> ResponseEntity<T> communicate(HttpMethod httpMethod, String url, String requestBody, String transactionName, String transactionId
			, String e2eAdmin, String e2ePassword, Class<T> clazz) {
		RestTemplate restTemplate = null;
		// not test https
		if (url.toLowerCase().startsWith("https")) {
			CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
			HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
			requestFactory.setHttpClient(httpClient);
			requestFactory.setConnectTimeout(10*1000);
			restTemplate = new RestTemplate(requestFactory);
		} else {
			restTemplate = new RestTemplate();
		}
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(StandardCharsets.UTF_8));
		
		HttpHeaders headers = new HttpHeaders();
		List<MediaType> mediaTypes = new ArrayList<>();
		mediaTypes.add(MediaType.APPLICATION_JSON);
		headers.setAccept(mediaTypes);
		headers.set(transactionName, transactionId);
		if( StringUtil.isNull(e2eAdmin) == false && StringUtil.isNull(e2ePassword) == false ) {
			headers.set("Authorization", "Basic " 
					+ Base64Utils.encodeToString(String.format("{}:{}", e2eAdmin, e2ePassword).getBytes()));
		}
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);
		
		log.info("Start to communicate, method[{}], url[{}], header[{}], body[{}])", httpMethod, url, headers.toString(), requestBody);
		
		return restTemplate.exchange(url, httpMethod, request, clazz);
	}
	
}
