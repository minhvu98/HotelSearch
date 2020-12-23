package com.spring.dataconfig;

import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConfig {
	@SuppressWarnings("unused")
	private final MongoProperties mongoProperties;

	public DataSourceConfig(MongoProperties mongoProperties) {
		this.mongoProperties = mongoProperties;
	}
}
