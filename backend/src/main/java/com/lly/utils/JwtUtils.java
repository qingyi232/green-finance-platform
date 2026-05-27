package com.lly.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtils {
    private static final String SECRET = "linlintong-greenfin-jwt-secret-key-for-eco-bank-2026";
    private static final long EXPIRE_MILLIS = 7 * 24 * 60 * 60 * 1000L; // 7 天

    private static SecretKey getKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public static String generateToken(Long userId, String username, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + EXPIRE_MILLIS);
        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public static Claims parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build()
                .parseClaimsJws(token).getBody();
    }

    public static Date getExpireTime() {
        return new Date(System.currentTimeMillis() + EXPIRE_MILLIS);
    }
}
