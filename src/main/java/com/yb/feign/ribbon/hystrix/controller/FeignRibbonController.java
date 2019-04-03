package com.yb.feign.ribbon.hystrix.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.yb.feign.ribbon.hystrix.dao.HelloFeignDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * Description:
 * author biaoyang
 * date 2019/4/3 000316:03
 */
@RestController
@CrossOrigin
public class FeignRibbonController {

    private final HelloFeignDao helloFeignDao;
    private final RestTemplate restTemplate;

    @Autowired
    public FeignRibbonController(HelloFeignDao helloFeignDao,RestTemplate restTemplate) {
        this.helloFeignDao = helloFeignDao;
        this.restTemplate = restTemplate;
    }

    /**
     * 这种需要写接口的方式就是feign了,而且feign还需要在配置文件类设置开启熔断才行(实测),可以看出ribbon简便很多
     * @param name
     * @return
     */
    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return helloFeignDao.hello(name);
    }

    /**
     * 直接使用注解注定回调熔断的方法,这种就适合ribbon
     * @return
     */
    @HystrixCommand(fallbackMethod = "ribbonFallback")
    @GetMapping("/world")
    public String world() {
        String body = restTemplate.getForEntity("http://producer-service/world", String.class).getBody();
        return body;
    }

    /**
     * 熔断调用方法
     * @return
     */
    public String ribbonFallback(){
        return "I'm fallback from ribbon";
    }
}
