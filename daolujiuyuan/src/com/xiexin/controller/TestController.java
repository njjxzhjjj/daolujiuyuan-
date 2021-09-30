package com.xiexin.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api/test")
@RestController
public class TestController {
    @RequestMapping("/test01") //  /api/test/test01
    public Map test01(){
        System.out.println("true = " + true);
        Map codeMap=new HashMap();
        codeMap.put("code",0);
        codeMap.put("msg","测试");
        return codeMap;
    }
}
