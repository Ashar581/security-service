package com.security.service.Service;

import com.security.service.DTO.UserDto;
import com.security.service.Entity.Profile;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    Boolean addProfilePicture(MultipartFile profile, String email);
    @Transactional
    Boolean remove(String email);
    Profile view(String email);
}
