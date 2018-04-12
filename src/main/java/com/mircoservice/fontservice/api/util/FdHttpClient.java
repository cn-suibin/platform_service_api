/*
 * API-GATEWAY调用httpclient
 * BY SUIBIN
 * 微服务间的接口调用。性能低于直接MVC,原则减少三方调用。
 * 便于扩展
 * */
package com.mircoservice.fontservice.api.util;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//调用服务中的接口
@FeignClient(name = "API-GATEWAY", fallback = FdHttpClientFallback.class)
public interface FdHttpClient {

    @RequestMapping(value="/base/hello2", method=RequestMethod.GET)
    public String syncFont1Feign(@PathVariable("jsonparam") String jsonparam);

    @RequestMapping(value="/base/hello2", method=RequestMethod.GET)
    public String syncFont2Feign();
}


