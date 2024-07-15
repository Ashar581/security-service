package com.security.service.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.security.service.Entity.File;
import com.security.service.Entity.Location;
import com.security.service.Entity.Profile;
import com.security.service.Entity.User;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    @JsonIgnore
    private String password;
    private Instant createTime;
    private Instant updateTime;
    @JsonIgnore
    private Location location;
    private List<File> files;
    private Profile profile;
    private List<String> allowedUsers;
    public static User dtoToEntity(UserDto dto){
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setCreateTime(dto.getCreateTime());
        user.setUpdateTime(dto.getUpdateTime());
        user.setFiles(dto.getFiles());
        user.setLocation(dto.getLocation());
        user.setFiles(dto.getFiles());
        user.setProfile(dto.getProfile());
        user.setAllowedUsers(dto.getAllowedUsers());
        return user;
    }
    public static UserDto entityToDto(User user){
        UserDto dto =  new UserDto();
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setFiles(user.getFiles());
        dto.setCreateTime(user.getCreateTime());
        dto.setUpdateTime(user.getUpdateTime());
        dto.setLocation(user.getLocation());
        dto.setProfile(user.getProfile());
        dto.setFiles(user.getFiles());
        dto.setAllowedUsers(user.getAllowedUsers());

        return dto;
    }

}
