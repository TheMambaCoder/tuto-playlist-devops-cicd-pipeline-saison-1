package com.example.demodevopscicdproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController()
@RequestMapping("/test")
public class DemoDevopsCicdProjectApplication {

    record ApiResponse(int code, String message, String environment) {
    };

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse testService() {
        return new ApiResponse(HttpStatus.OK.value(), "YOUPIIIII I'm running.....", "Prod");
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoDevopsCicdProjectApplication.class, args);
    }

}
