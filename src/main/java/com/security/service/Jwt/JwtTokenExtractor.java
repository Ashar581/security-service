package com.security.service.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JwtTokenExtractor {
    @Autowired
    KeyConfig keyConfig;

    public List<String> getAllowedUserList(String token){
        Map<String,Object> map = getClaims(token).getBody().get("Allowed",Map.class);
        List<String> allowedUsers = (List<String>) map.get("view");
        return allowedUsers;
    }

    private Jws<Claims> getClaims(String token){
        Jws<Claims> claims = Jwts.parserBuilder()
                .setSigningKey(keyConfig.getKey())
                .build()
                .parseClaimsJws(token);
        return claims;
    }
}
