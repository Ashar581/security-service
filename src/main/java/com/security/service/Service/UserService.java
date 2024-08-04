package com.security.service.Service;

import com.security.service.DTO.UserDto;
import com.security.service.Entity.SOS;
import com.security.service.Entity.User;
import com.security.service.Model.LoginRequest;
import com.security.service.Model.LoginResponse;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserDto add(User user);
    List<User> get();
    LoginResponse authenticate(LoginRequest request) throws MessagingException;
    UserDto update(UserDto userDto);
    User view(String email);
    @Transactional
    void delete(String email);
    Set<String> addWatchLive(UserDto userDto, String email);
    @Transactional
    Set<String> addLiveListeners(UserDto dto, String email);
    List<String> searchEmail(String search);
    @Transactional
    SOS addSos(UserDto dto, String email);
    @Transactional
    SOS initateSOS(String email);
}
