/**
 * Copyright (C) 2018-2019
 * All rights reserved, Designed By www.joolun.com
 * 注意：
 * 本软件为www.joolun.com开发研制，项目使用请保留此说明
 */
package com.joolun.mall.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * reids相关配置
 *
 * @author
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisConfigProperties {

	private String host;
	private String port;
	private String password;
	private String database;

}
