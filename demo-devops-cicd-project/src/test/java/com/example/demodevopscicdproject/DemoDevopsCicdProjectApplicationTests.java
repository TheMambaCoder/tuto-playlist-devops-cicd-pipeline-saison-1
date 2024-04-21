package com.example.demodevopscicdproject;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class DemoDevopsCicdProjectApplicationTests {

    @Autowired
    private DemoDevopsCicdProjectApplication demoDevopsCicdProjectApplication;

    @Test
    void contextLoads() {
    }

    @Test
    public void testTestService() {
        DemoDevopsCicdProjectApplication.ApiResponse result = demoDevopsCicdProjectApplication.testService();
        assertEquals("YOUPIIIII I'm running.....", result.message());
        assertEquals(200, result.code());
    }
}
