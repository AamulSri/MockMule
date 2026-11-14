
package com.mockmule.userservice.tests.security;

import com.mockmule.userservice.entity.User;
import com.mockmule.userservice.service.JwtService;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String TEST_SECRET =
            "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";

    private SecretKey secretKey;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService(TEST_SECRET);

        secretKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(TEST_SECRET)
        );
    }

    // Test 1: Generate JWT successfully
    @Test
    void shouldGenerateTokenSuccessfully() {

        User user = new User();
        user.setId(1L);
        user.setEmail("aamul@gmail.com");

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    // Test 2: Extract correct user ID
    @Test
    void shouldExtractUserIdFromToken() {

        User user = new User();
        user.setId(1L);
        user.setEmail("aamul@gmail.com");

        String token = jwtService.generateToken(user);

        Long extractedUserId = jwtService.extractUserId(token);

        assertEquals(1L, extractedUserId);
    }

    // Test 3: Reject invalid JWT
    @Test
    void shouldRejectInvalidToken() {

        String invalidToken = "invalid.jwt.token";

        assertThrows(
                JwtException.class,
                () -> jwtService.extractUserId(invalidToken)
        );
    }

    // Test 4: Reject expired JWT
    @Test
    void shouldRejectExpiredToken() {

        Date issuedAt = new Date(
                System.currentTimeMillis() - 7200000
        );

        Date expiration = new Date(
                System.currentTimeMillis() - 3600000
        );

        String expiredToken = Jwts.builder()
                .subject("1")
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();

        assertThrows(
                JwtException.class,
                () -> jwtService.extractUserId(expiredToken)
        );
    }
}
