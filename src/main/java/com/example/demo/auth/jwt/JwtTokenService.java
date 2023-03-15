package com.example.demo.auth.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
@Service
public class JwtTokenService {
    @Value ("${jwt.secret}")
    private String secretKey;

    public String createAccessToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(Duration.ofMinutes(15))))
                .signWith(SignatureAlgorithm.HS256, "demoApplicationv3")
                .compact();
    }
    public String createRefreshToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(Duration.ofDays(7))))
                .signWith(SignatureAlgorithm.HS256, "demoApplicationv3")
                .compact();
    }
}
