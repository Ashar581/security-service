package com.security.service.Controller;

import com.security.service.ApiResponse.ApiResponse;
import com.security.service.ApiResponse.ApiUtils;
import com.security.service.DTO.UserDto;
import com.security.service.Entity.Profile;
import com.security.service.Service.ProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("api/profile")
public class ProfileController extends ApiUtils {
    @Autowired
    ProfileService profileService;
    @PutMapping("add")
    ResponseEntity<ApiResponse<Boolean>> addProfilePicture(@RequestParam("profilePicture")MultipartFile profilePicture, HttpServletRequest request) {
        return getApiResponse(profileService.addProfilePicture(profilePicture,request.getAttribute("email").toString()),
                "Profile Picture Added");
    }
    @DeleteMapping("remove")
    ResponseEntity<ApiResponse<Boolean>> removeProfilePic(HttpServletRequest request){
        return getApiResponse(profileService.remove(request.getAttribute("email").toString()),"Profile Pic Removed");
    }
    @GetMapping("view")
    ResponseEntity<ApiResponse<Profile>> view(HttpServletRequest request){
        return getApiResponse(profileService.view(request.getAttribute("email").toString()),"Profile Picture Displayed");
    }
}
