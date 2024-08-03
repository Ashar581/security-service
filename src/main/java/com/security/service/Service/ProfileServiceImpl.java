package com.security.service.Service;

import com.security.service.DTO.UserDto;
import com.security.service.Entity.Profile;
import com.security.service.Entity.User;
import com.security.service.Exceptions.ProfilePictureException;
import com.security.service.Exceptions.UserNotFoundException;
import com.security.service.Repository.ProfileRepo;
import com.security.service.Repository.UserRepo;
import com.security.service.Util.ImageCompressionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@Service
public class ProfileServiceImpl implements ProfileService{
    @Autowired
    ProfileRepo profileRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    ImageCompressionUtil imageCompressionUtil;
    @Override
    @CacheEvict(value = "profilePictureCache", allEntries = true)
    public Boolean addProfilePicture(MultipartFile profilePic, String email) throws UserNotFoundException, ProfilePictureException {
        //add profile to user, if doesn't exist else update.
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        Profile profile = user.getProfile();
        if (Objects.isNull(profile)){
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }
        try {
            profile.setProfileName(profilePic.getOriginalFilename());
            profile.setProfileType(profilePic.getContentType());
            profile.setData(imageCompressionUtil.compressImage(profilePic));
        }catch (Exception e){
            throw new ProfilePictureException("Profile Picture Cannot Be Added. Try again!");
        }

        userRepo.save(user);

        return true;
    }

    @Override
    @CacheEvict(value = "profilePictureCache", allEntries = true)
    public Boolean remove(String email) throws UserNotFoundException, ProfilePictureException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        Profile profile = user.getProfile();
        if (Objects.isNull(profile)) throw new ProfilePictureException("Profile Picture Does Not Exists");
        user.setProfile(null);
        profileRepo.delete(profile);
        return true;
    }
    @Override
    @Cacheable("profilePictureCache")
    public Profile view(String email) throws UserNotFoundException,ProfilePictureException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        Profile profile = user.getProfile();
        if (Objects.isNull(profile)) throw new ProfilePictureException("No Profile Picture Found");

        return profile;
    }
}
