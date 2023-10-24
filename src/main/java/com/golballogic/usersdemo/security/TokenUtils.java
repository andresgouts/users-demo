package com.golballogic.usersdemo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtils {

    private final static String SECRETE = "Eyc0GQm40mubVCjiM3uy9rWrZCE8BeUG";
    private final static Long TOKEN_TTL = 86_400L;

    public static String createToken(String name, String email) {
        long tokenTimeToLive = TOKEN_TTL * 1000;
        Date expirationDate = new Date(System.currentTimeMillis() + tokenTimeToLive);

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("name", name);

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(expirationDate)
                .addClaims(userInfo)
                .signWith(Keys.hmacShaKeyFor(SECRETE.getBytes()))
                .compact();
    }

    public static UsernamePasswordAuthenticationToken authenticateUser(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SECRETE.getBytes())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            String email = claims.getSubject();

            return new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());

        } catch (JwtException e) {
            return null;
        }

    }

}
