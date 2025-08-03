package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping
    public String hello(){
        // 의도적 코드 수정
        // commit for trigger 
        return "Hello World: v1";
    }
}
