package com.weebeeio.demo.domain;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "swagger 테스트 API", description = "swagger 테스트를 진행하는 API")
@RestController   // ← @Controller 대신
@RequestMapping("/Hello")
public class HelloController {

    @Operation(summary = "hello", description = "서버응답을 테스트합니다.")
    @GetMapping("/test")
    public String helloTest(){
        return "hello";   // 그대로 "hello" 텍스트가 응답됩니다.
    }
}
