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
 * Description:注意使用熔断器的仪表盘查看相关配置
 * 因为是服务间的调用需要熔断,所以才会有feign和ribbon的调用熔断,网关进来是直接路由到指定的服务,
 * 如果没有在服务里调用其他的服务,那么就不需要熔断,需要熔断都是服务调用服务的情况,解决调用服务的异常而导致自己的服务也异常了
 * 如果自己的没有调用其他的服务,除了异常了就直接报异常了,根本不会传递异常给谁,除非别的服务调用你,又没有做熔断处理
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
     * 注意使用熔断器的仪表盘查看相关配置
     * 这种需要写接口的方式就是feign了,而且feign还需要在配置文件类设置开启熔断才行(实测),可以看出ribbon简便很多
     * @param name
     * @return
     */
    @GetMapping("/hello")
    public String hello(@RequestParam String name) {
        return helloFeignDao.hello(name);
    }

    /**
     * 注意使用熔断器的仪表盘查看相关配置
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
     * 注意使用熔断器的仪表盘查看相关配置
     * 熔断调用方法
     * @return
     */
    public String ribbonFallback(){
        return "I'm fallback from ribbon";
    }
}
