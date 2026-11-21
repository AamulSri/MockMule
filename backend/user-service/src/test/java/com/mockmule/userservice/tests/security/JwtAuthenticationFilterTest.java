
package com.mockmule.userservice.tests.security;

import com.mockmule.userservice.config.JwtAuthenticationFilter;
import com.mockmule.userservice.entity.User;
import com.mockmule.userservice.enums.UserRole;
import com.mockmule.userservice.repository.UserRepository;
import com.mockmule.userservice.service.JwtService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    // Test 1: Valid JWT authenticates user
    @Test
    void shouldAuthenticateUserWithValidToken()
            throws ServletException, IOException {

        request.addHeader("Authorization", "Bearer valid-token");

        User user = new User();
        user.setId(1L);
        user.setUsername("Aamul");
        user.setEmail("aamul@gmail.com");
        user.setRole(UserRole.USER);

        when(jwtService.extractUserId("valid-token"))
                .thenReturn(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        jwtAuthenticationFilter.doFilter(
                request, response, filterChain
        );

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        assertNotNull(authentication);
        assertEquals(user, authentication.getPrincipal());
        assertTrue(authentication.isAuthenticated());
        assertTrue(authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_USER")));

        verify(filterChain).doFilter(request, response);
    }

    // Test 2: Missing JWT does not authenticate user
    @Test
    void shouldContinueWithoutAuthenticationWhenTokenIsMissing()
            throws ServletException, IOException {

        jwtAuthenticationFilter.doFilter(
                request, response, filterChain
        );

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(jwtService);
    }

    // Test 3: Invalid JWT is rejected
    @Test
    void shouldRejectInvalidToken()
            throws ServletException, IOException {

        request.addHeader("Authorization", "Bearer invalid-token");

        when(jwtService.extractUserId("invalid-token"))
                .thenThrow(new JwtException("Invalid token"));

        jwtAuthenticationFilter.doFilter(
                request, response, filterChain
        );

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
        verifyNoInteractions(userRepository);
    }

    // Test 4: Expired JWT is rejected
    @Test
    void shouldRejectExpiredToken()
            throws ServletException, IOException {

        request.addHeader("Authorization", "Bearer expired-token");

        when(jwtService.extractUserId("expired-token"))
                .thenThrow(new ExpiredJwtException(
                        null, null, "Token expired"
                ));

        jwtAuthenticationFilter.doFilter(
                request, response, filterChain
        );

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
    }

    // Test 5: Deleted user cannot authenticate
    @Test
    void shouldNotAuthenticateWhenUserDoesNotExist()
            throws ServletException, IOException {

        request.addHeader("Authorization", "Bearer valid-token");

        when(jwtService.extractUserId("valid-token"))
                .thenReturn(1L);

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        jwtAuthenticationFilter.doFilter(
                request, response, filterChain
        );

        assertNull(
                SecurityContextHolder.getContext().getAuthentication()
        );

        verify(filterChain).doFilter(request, response);
    }
}
