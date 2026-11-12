package com.mockmule.userservice.controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/user")
public class HealthController {

    @GetMapping("/health")
    public String health(){
        return "MockMule User Service is up and running!";
    }
}
