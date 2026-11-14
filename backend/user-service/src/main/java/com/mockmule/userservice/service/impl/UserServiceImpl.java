package com.mockmule.userservice.service.impl;

import com.mockmule.userservice.dto.request.LoginRequest;
import com.mockmule.userservice.dto.request.RegisterRequest;
import com.mockmule.userservice.dto.response.LoginResponse;
import com.mockmule.userservice.dto.response.RegisterResponse;
import com.mockmule.userservice.entity.User;
import com.mockmule.userservice.exception.InvalidCredentialsException;
import com.mockmule.userservice.exception.UserAlreadyExistsException;
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

        String email = registerRequest.getEmail().trim();
        String username = registerRequest.getUsername().trim();

        if(userRepository.existsByEmail(email)){
            throw new UserAlreadyExistsException("Email already registered");
        }
        if(userRepository.existsByUsername(username)){
            throw new UserAlreadyExistsException("Username already exists");
        }

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
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
        String email = loginRequest.getEmail().trim();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        boolean passwordMatcher = passwordEncoder.matches(loginRequest.getPassword(), user.getPassword());

        if(!passwordMatcher){
            throw new InvalidCredentialsException("Invalid email or password");
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
