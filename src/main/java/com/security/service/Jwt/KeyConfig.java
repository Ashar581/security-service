package com.security.service.Jwt;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@Data
public class KeyConfig {
    @Value("${secret}")
    private String secret;
    @Value(("${rtexpirationmins}"))
    private Long rtexpiration;
    @Value(("${atexpirationmins}"))
    private Long atexpiration;

    public Key getKey(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
