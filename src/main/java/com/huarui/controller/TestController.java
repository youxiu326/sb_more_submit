package com.huarui.controller;

import com.huarui.common.ConstantUtils;
import com.huarui.util.ApiRepeatSubmit;
import com.huarui.util.ApiToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class TestController {


    /**
     * 进入页面
     * @return
     */
    @GetMapping("/")
    @ApiToken
    public String index(){
        return "index";
    }


    /**
     * 测试重复提交接口
     * 将Token放入请求头中
     * @return
     */
    @RequestMapping("/test")
    @ApiRepeatSubmit(ConstantUtils.HEAD)
    public @ResponseBody String test() {
        return ("程序逻辑返回");
    }

} 