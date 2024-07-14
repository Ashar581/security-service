package com.security.service.Jwt;

import com.security.service.Entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class JwtUtil {
    @Value("${secret}")
    private String secret;
    @Value(("${rtexpirationmins}"))
    private Long rtexpiration;
    @Value(("${atexpirationmins}"))
    private Long atexpiration;

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
                        .plusMinutes(rtexpiration)
                        .atZone(ZoneId.systemDefault()).toInstant()))
                .claim("Allowed",allowed)
                .signWith(getKey())
                .compact();
    }

    private Key getKey(){
        byte [] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
