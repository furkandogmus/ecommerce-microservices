package com.hoangtien2k3.userservice.security.validate;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.List;

public class AuthorityTokenUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public List<String> checkPermission(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            @SuppressWarnings("unchecked")
            List<String> authorities = claims.get("authorities", List.class);
            return authorities != null ? authorities : new ArrayList<>();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

}
