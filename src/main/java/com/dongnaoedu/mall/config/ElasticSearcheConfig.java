package com.dongnaoedu.mall.config;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 将ES客户端交给Spring容器管理，并处理client的回收处理
 *
 */
@Configuration
public class ElasticSearcheConfig {
	
	@Value("${ES_CONNECT_IP}")
	private String ES_CONNECT_IP;

	@Value("${ES_CLUSTER_NAME}")
	private String ES_CLUSTER_NAME;
	
	@Bean
	@SuppressWarnings("resource")
	public TransportClient initEsClient() throws UnknownHostException {
		Settings settings = Settings.builder()
				.put("cluster.name", ES_CLUSTER_NAME)
				.put("client.transport.sniff", true)
				.build();
		
		TransportClient client = new PreBuiltTransportClient(settings)
				.addTransportAddress(new TransportAddress(InetAddress.getByName(ES_CONNECT_IP), 9300));
		
		Runtime.getRuntime().addShutdownHook(new Thread(()-> {
			client.close();
		}));
		
		return client;
	}
	
}
