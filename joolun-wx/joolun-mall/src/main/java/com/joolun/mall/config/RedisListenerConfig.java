package com.joolun.mall.config;

import cn.hutool.core.util.StrUtil;
import com.joolun.mall.listener.RedisKeyExpirationListener;
import com.joolun.mall.service.OrderInfoService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author www.joolun.com
 */
@Configuration
@AllArgsConstructor
public class RedisListenerConfig {

	private final RedisTemplate<String, String> redisTemplate;
	private final RedisConfigProperties redisConfigProperties;
	private final OrderInfoService orderInfoService;

	@Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory) {

		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
		container.setConnectionFactory(connectionFactory);
		container.addMessageListener(new RedisKeyExpirationListener(redisTemplate, redisConfigProperties, orderInfoService), new PatternTopic(StrUtil.format("__keyevent@{}__:expired", redisConfigProperties.getDatabase())));
		return container;
	}
}

