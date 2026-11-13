package com.mockmule.userservice.service;

import com.mockmule.userservice.dto.request.LoginRequest;
import com.mockmule.userservice.dto.request.RegisterRequest;
import com.mockmule.userservice.dto.response.LoginResponse;
import com.mockmule.userservice.dto.response.RegisterResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest registerRequest);

    LoginResponse login (LoginRequest loginRequest);
}
