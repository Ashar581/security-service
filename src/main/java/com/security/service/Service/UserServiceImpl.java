package com.security.service.Service;

import com.security.service.DTO.UserDto;
import com.security.service.Entity.Location;
import com.security.service.Entity.User;
import com.security.service.Exceptions.IncorrectPasswordException;
import com.security.service.Exceptions.UserNotFoundException;
import com.security.service.Jwt.JwtTokenGenerator;
import com.security.service.Model.LoginRequest;
import com.security.service.Model.LoginResponse;
import com.security.service.Repository.UserRepo;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    HtmlCreator creator;
    @Autowired
    MailService mailService;
    @Autowired
    UserRepo userRepo;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    JwtTokenGenerator jwtUtil;

    @Override
    public UserDto add(User user) throws UserNotFoundException {
        if(userRepo.existsByEmail(user.getEmail())) throw new UserNotFoundException("User Exists");
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreateTime(Instant.now());
        user.setLocation(new Location());
        return UserDto.entityToDto(userRepo.save(user));
    }

    @Override
    public List<User> get() throws UserNotFoundException{
        return userRepo.findAll();
    }

    @Override
    public LoginResponse authenticate(LoginRequest request) throws UserNotFoundException, IncorrectPasswordException, MessagingException {
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

        mailService.sendHtmlEmail(request.getUsername(), "Account Was Logged In",creator.getHtml(user.getFirstName()));
        
        return response;
    }

    @Override
    public UserDto update(UserDto dto) {
        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(()-> new UserNotFoundException("User Does Not Exits"));
        Boolean update = false;
        if (Objects.nonNull(dto.getFirstName())) user.setFirstName(dto.getFirstName());update = true;
        if (Objects.nonNull(dto.getLastName())) user.setLastName(dto.getLastName());update = true;
        if (Objects.nonNull(dto.getPhoneNumber())) user.setPhoneNumber(dto.getPhoneNumber());update = true;
        if (Objects.nonNull(dto.getAllowedUsers())) user.setAllowedUsers(dto.getAllowedUsers());update = true;
        if (update) user.setUpdateTime(Instant.now());
        System.out.println("User Update: "+user.getPhoneNumber());

        return UserDto.entityToDto(userRepo.save(user));
    }

    @Override
    public User view(String email) throws UserNotFoundException{
        return userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
    }

    @Override
    public void delete(String email) throws UserNotFoundException {
        User user = userRepo.findByEmail(email)
                        .orElseThrow(()->new UserNotFoundException("User Not Found"));
        userRepo.delete(user);
    }
}
