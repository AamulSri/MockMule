package com.mockmule.userservice.service;

import com.mockmule.userservice.dto.request.RegisterRequest;
import com.mockmule.userservice.dto.response.RegisterResponse;

public interface UserService {

    RegisterResponse register(RegisterRequest registerRequest);
}
