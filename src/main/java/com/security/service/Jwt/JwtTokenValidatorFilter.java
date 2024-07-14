package com.security.service.Jwt;

import com.security.service.Exceptions.TokenNotValidException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

@Component
public class JwtTokenValidatorFilter extends OncePerRequestFilter {
    @Autowired
    JwtTokenValidator jwtTokenValidator;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (Objects.nonNull(request.getHeader("Authorization"))) {
            try {
                String token = request.getHeader("Authorization").replace("Bearer ", "").trim();
                System.out.println("Token: " + token);
                if (jwtTokenValidator.isTokenValid(token)) throw new TokenNotValidException("Token Expired");
                //principal-useremail(id),credentials-user creds,authorities-role of the user.
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(jwtTokenValidator.getUsername(token), null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("Authenticated? " + authentication.isAuthenticated());
                request.setAttribute("email", jwtTokenValidator.getUsername(token));
            }catch (TokenNotValidException te){
                throw new TokenNotValidException("Token Expired");
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().write("JWT Token Validation Failed");
            }
        }
        filterChain.doFilter(request,response);
    }
}
