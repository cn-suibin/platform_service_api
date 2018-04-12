/*
 * 
 * 禁用hystrix配置，请在相应位置设置
 * @FeignClient(name="HELLO-SERVICE",configuration=DisableHystrixConfiguration.class)
 * 
 * */
package com.mircoservice.fontservice.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

import feign.Feign;

public class DisableHystrixConfiguration {

	@Bean
	@Scope("prototype")
	public Feign.Builder feignBuilder(){
		return Feign.builder();
	}
}
