package com.security.service.Jwt;

import com.security.service.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class JwtTokenGenerator {
@Autowired
KeyConfig keyConfig;

    public String generateToken(User user){
        Map<String,Object> header = new HashMap<>();
        header.put("t", Base64.getEncoder().encode(user.getEmail().getBytes(StandardCharsets.UTF_8)));

        Map<String,Object> allowed = new HashMap<>();
        allowed.put("Guardian",user.getAllowedUsers());

        return Jwts.builder()
                .setHeader(header)
                .setSubject(user.getFirstName()+" "+user.getLastName())
                .setId(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(Date.from(LocalDateTime.now()
                        .plusMinutes(keyConfig.getRtexpiration())
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .claim("Allowed",allowed)
                .signWith(keyConfig.getKey())
                .compact();
    }

}
