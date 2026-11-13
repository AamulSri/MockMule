package com.mockmule.userservice.service.impl;

import com.mockmule.userservice.dto.request.LoginRequest;
import com.mockmule.userservice.dto.request.RegisterRequest;
import com.mockmule.userservice.dto.response.LoginResponse;
import com.mockmule.userservice.dto.response.RegisterResponse;
import com.mockmule.userservice.entity.User;
import com.mockmule.userservice.repository.UserRepository;
import com.mockmule.userservice.service.JwtService;
import com.mockmule.userservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public RegisterResponse register(RegisterRequest registerRequest){

        if(userRepository.existsByEmail(registerRequest.getEmail())){
            throw new RuntimeException("Email already exists");
        }
        if(userRepository.existsByUsername(registerRequest.getUsername())){
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        User savedUser = userRepository.save(user);

        return new RegisterResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                "User registered successfully"
        );
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        // Implementation for login logic
        String email = loginRequest.getEmail();
        User user = userRepository.findByEmail(email).
                orElseThrow(()-> new RuntimeException("User Not Found with email:"+email));

        boolean passwordMatcher = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if(!passwordMatcher){
            throw new RuntimeException("Invalid Password");
        }
        return new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                jwtService.generateToken(user),
                "User logged in successfully"
        );
    }



}
