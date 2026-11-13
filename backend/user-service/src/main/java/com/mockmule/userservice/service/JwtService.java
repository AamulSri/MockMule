package com.mockmule.userservice.service;


import com.mockmule.userservice.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;

    private static final long EXPIRATION_TIME = 60*60*1000; // 1 hour in milliseconds

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
    }

    public String generateToken(User user){
        Date issuedAt = new Date();
        Date expiredAt = new Date(issuedAt.getTime() + EXPIRATION_TIME);

        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .claim("email",user.getEmail())
                .issuedAt(issuedAt)
                .expiration(expiredAt)
                .signWith(secretKey)
                .compact();
    }

    public long extractUserId(String token) {
    Claims claim = Jwts.parser().verifyWith(secretKey)
            .build().parseSignedClaims(token)
            .getPayload();

    return Long.valueOf(claim.getSubject());
    }

}
