package com.mockmule.userservice.controller;

import com.mockmule.userservice.dto.request.RegisterRequest;
import com.mockmule.userservice.dto.response.RegisterResponse;
import com.mockmule.userservice.entity.User;
import com.mockmule.userservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    private ResponseEntity<RegisterResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest)
    {
        RegisterResponse response = userService.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

}
