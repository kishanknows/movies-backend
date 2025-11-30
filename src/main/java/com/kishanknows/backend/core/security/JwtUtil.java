package com.kishanknows.backend.core.security;

import com.kishanknows.backend.user.model.User;
import com.kishanknows.backend.user.model.UserEntity;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION = 1000 * 60 * 15;

    public static String generateAccessToken(Long userId, String name, String email){
        return Jwts.builder()
                .claim("id", userId)
                .claim("name", name)
                .claim("email", email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key)
                .compact();
    }

    public static Key getKey(){
        return key;
    }
}
