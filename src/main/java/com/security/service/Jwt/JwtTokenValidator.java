package com.security.service.Jwt;

import com.security.service.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@Component
public class JwtTokenValidator {
    @Autowired
    KeyConfig keyConfig;

    //write the code to extract the Guardian list...
    public Boolean isTokenValid(String token){
        return (isTokenExpired(token));
    }
    private Boolean isTokenExpired(String token){
        return getJws(token).getBody().getExpiration().before(new Date());
    }
    public String getUsername(String token){
        return getJws(token).getBody().getId();
    }
    private Jws<Claims> getJws(String token){
        Jws<Claims> jws = Jwts.parserBuilder()
                .setSigningKey(keyConfig.getKey())
                .build()
                .parseClaimsJws(token);
        return jws;
    }
}
