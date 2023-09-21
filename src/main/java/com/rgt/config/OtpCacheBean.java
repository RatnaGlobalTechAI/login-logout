package com.rgt.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@Configuration
public class OtpCacheBean {
	
	
	@Value("${expiration-minutes}")
	private String expirationMinutes;
	
	
	

	@Bean
	public LoadingCache<String, Integer> loadingCache() {
		return CacheBuilder.newBuilder().expireAfterWrite(Integer.parseInt(expirationMinutes), TimeUnit.MINUTES)
				.build(new CacheLoader<>() {
					public Integer load(String key) {
						return 0;
					}

				});
	}




	

}