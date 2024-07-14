package com.security.service.Service;

import com.security.service.DTO.UserDto;
import com.security.service.Entity.User;
import com.security.service.Model.LoginRequest;
import com.security.service.Model.LoginResponse;

import java.util.List;

public interface UserService {
    UserDto add(User user);
    List<User> get();
    LoginResponse authenticate(LoginRequest request);
}
