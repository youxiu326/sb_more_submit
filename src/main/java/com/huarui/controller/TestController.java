package com.huarui.controller;


import com.huarui.util.NoRepeatSubmit;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * 同一客户端在2秒内对同一URL的提交视为重复提交
 *
 * @功能描述 测试Controller
 * @author www.gaozz.club
 * @date 2018-08-26
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    @NoRepeatSubmit
    public String test() {
        return ("程序逻辑返回");
    }

} 