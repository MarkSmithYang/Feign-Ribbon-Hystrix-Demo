package com.yb.feign.ribbon.hystrix.dao;

import com.yb.feign.ribbon.hystrix.fallback.FallbackHelloFeign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Description:
 * author biaoyang
 * date 2019/4/3 000315:43
 */
@FeignClient(name = "producer-service",fallback = FallbackHelloFeign.class)
public interface HelloFeignDao {

    /**
     * 远程调用方法
     * @param name
     * @return
     */
    @GetMapping("/hello")
    String hello(@RequestParam String name);
}
