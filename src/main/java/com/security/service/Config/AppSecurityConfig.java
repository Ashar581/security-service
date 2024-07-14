package com.security.service.Config;

import com.security.service.Jwt.JwtTokenValidatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@CrossOrigin
public class AppSecurityConfig {
    @Autowired
    JwtTokenValidatorFilter jwtTokenValidatorFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf->csrf.disable())
                .cors(cors -> cors
                        .configurationSource(request -> {
                            CorsConfiguration configuration = new CorsConfiguration();
                            configuration.setAllowedOrigins(Collections.singletonList("*"));
                            configuration.setAllowedHeaders(Arrays.asList("Authorization","Content-Type","Accept","Origin"));
                            configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
                            configuration.addExposedHeader("Authorization");

                            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                            source.registerCorsConfiguration("/api/**",configuration);
                            return configuration;
                        })
                )
                .authorizeRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,"api/user/view").authenticated()
                        .anyRequest().permitAll())
                .addFilterAfter(jwtTokenValidatorFilter, BasicAuthenticationFilter.class)
                .httpBasic(withDefaults());
        return httpSecurity.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){return new BCryptPasswordEncoder();}
}

