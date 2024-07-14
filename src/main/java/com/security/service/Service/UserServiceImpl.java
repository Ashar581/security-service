package com.security.service.Service;

import com.security.service.DTO.UserDto;
import com.security.service.Entity.User;
import com.security.service.Exceptions.IncorrectPasswordException;
import com.security.service.Exceptions.UserNotFoundException;
import com.security.service.Jwt.JwtUtil;
import com.security.service.Model.LoginRequest;
import com.security.service.Model.LoginResponse;
import com.security.service.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepo userRepo;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    JwtUtil jwtUtil;

    @Override
    public UserDto add(User user) throws UserNotFoundException {
        if(userRepo.existsByEmail(user.getEmail())) throw new UserNotFoundException("User Exists");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreateTime(Instant.now());
        return UserDto.entityToDto(userRepo.save(user));
    }

    @Override
    public List<User> get() throws UserNotFoundException{
        return userRepo.findAll();
    }

    @Override
    public LoginResponse authenticate(LoginRequest request) throws UserNotFoundException, IncorrectPasswordException {
        System.out.println("UserName: "+request.getUsername());
        User user = userRepo.findByEmail(request.getUsername())
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if (!bCryptPasswordEncoder.matches(request.getPassword(),user.getPassword()))
            throw new IncorrectPasswordException("Incorrect Password");

        LoginResponse response = new LoginResponse();

        String token = jwtUtil.generateToken(user);
        response.setToken(token);
        response.setRefreshToken(token);

        response.setUserDto(UserDto.entityToDto(user));
        return response;
    }
}
