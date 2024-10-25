package com.joolun.web.core.config;

import com.joolun.web.core.resolver.SqlFilterArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author
 * @date
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	/**
	 * 增加请求参数解析器，对请求中的参数注入SQL
	 * @param argumentResolvers
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new SqlFilterArgumentResolver());
	}
}
