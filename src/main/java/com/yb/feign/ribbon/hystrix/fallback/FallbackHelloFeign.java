package com.yb.feign.ribbon.hystrix.fallback;

import com.yb.feign.ribbon.hystrix.dao.HelloFeignDao;
import org.springframework.stereotype.Component;

/**
 * Description:熔断回调类,继承feign远程调用接口,并在实现方法中输出回调的信息
 * author biaoyang
 * date 2019/4/3 000315:59
 */
@Component
public class FallbackHelloFeign implements HelloFeignDao {

    @Override
    public String hello(String name) {
        return "I'm Hystrix, the server is fail";
    }
}
