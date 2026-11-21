package com.mockmule.userservice.tests.service;

import com.mockmule.userservice.dto.request.LoginRequest;
import com.mockmule.userservice.dto.request.RegisterRequest;
import com.mockmule.userservice.dto.response.LoginResponse;
import com.mockmule.userservice.dto.response.RegisterResponse;
import com.mockmule.userservice.entity.User;
import com.mockmule.userservice.exception.InvalidCredentialsException;
import com.mockmule.userservice.exception.UserAlreadyExistsException;
import com.mockmule.userservice.repository.UserRepository;
import com.mockmule.userservice.service.JwtService;
import com.mockmule.userservice.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;



    @Test
    public void shoulRegisterUserSuccessfully(){
        RegisterRequest registerRequest = new RegisterRequest();

        registerRequest.setUsername("testuser");
        registerRequest.setEmail("test@example.com");
        registerRequest.setPassword("MockMule@123");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("MockMule@123")).thenReturn("encodedPassword");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("encodedPassword");

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        RegisterResponse response = userService.register(registerRequest);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("testuser", response.getUsername());
        Assertions.assertEquals("test@example.com", response.getEmail());

        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("MockMule@123");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Aamul");
        request.setEmail("aamul@gmail.com");
        request.setPassword("MockMule@123");

        when(userRepository.existsByEmail("aamul@gmail.com"))
                .thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.register(request)
        );

        verify(userRepository, Mockito.never())
                .save(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        // Arrange
        RegisterRequest request = new RegisterRequest();
        request.setUsername("Aamul");
        request.setEmail("new@gmail.com");
        request.setPassword("MockMule@123");

        when(userRepository.existsByEmail("new@gmail.com"))
                .thenReturn(false);

        when(userRepository.existsByUsername("Aamul"))
                .thenReturn(true);

        // Act & Assert
        Assertions.assertThrows(
                UserAlreadyExistsException.class,
                () -> userService.register(request)
        );

        verify(userRepository, Mockito.never())
                .save(any(User.class));
    }

    @Test
    void shouldLoginUserSuccessfully() {

        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("aamul@gmail.com");
        request.setPassword("MockMule@123");

        User user = new User();
        user.setId(1L);
        user.setUsername("Aamul");
        user.setEmail("aamul@gmail.com");
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail("aamul@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "MockMule@123", "hashedPassword"))
                .thenReturn(true);

        when(jwtService.generateToken(user))
                .thenReturn("mock-jwt-token");

        // Act
        LoginResponse response = userService.login(request);

        // Assert
        Assertions.assertNotNull(response);
        Assertions.assertEquals(1L, response.getId());
        Assertions.assertEquals("Aamul", response.getUsername());
        Assertions.assertEquals("mock-jwt-token", response.getToken());

        verify(passwordEncoder).matches(
                "MockMule@123", "hashedPassword");
        verify(jwtService).generateToken(user);
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsIncorrect() {

        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("aamul@gmail.com");
        request.setPassword("WrongPassword@123");

        User user = new User();
        user.setId(1L);
        user.setUsername("Aamul");
        user.setEmail("aamul@gmail.com");
        user.setPassword("hashedPassword");

        when(userRepository.findByEmail("aamul@gmail.com"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.matches(
                "WrongPassword@123", "hashedPassword"))
                .thenReturn(false);

        // Act & Assert
        Assertions.assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(request)
        );

        verify(jwtService, Mockito.never())
                .generateToken(any(User.class));
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotExist() {

        // Arrange
        LoginRequest request = new LoginRequest();
        request.setEmail("unknown@gmail.com");
        request.setPassword("MockMule@123");

        when(userRepository.findByEmail("unknown@gmail.com"))
                .thenReturn(Optional.empty());

        // Act & Assert
        Assertions.assertThrows(
                InvalidCredentialsException.class,
                () -> userService.login(request)
        );

        verify(passwordEncoder, Mockito.never())
                .matches(anyString(), anyString());

        verify(jwtService, Mockito.never())
                .generateToken(any(User.class));
    }
}
