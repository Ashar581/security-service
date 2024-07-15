package com.security.service.Service;

import com.security.service.Entity.Location;
import com.security.service.Entity.User;
import com.security.service.Exceptions.LocationIdNotFoundException;
import com.security.service.Exceptions.UserNotFoundException;
import com.security.service.Model.LocationResponse;
import com.security.service.Repository.LocationRepo;
import com.security.service.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class LocationServiceImpl implements LocationService{
    @Autowired
    UserRepo userRepo;
    @Autowired
    LocationRepo locationRepo;

    @Override
    public Location addLiveLocation(String email, LocationResponse response) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        Location location = user.getLocation();
        if (Objects.isNull(location)){
            location = new Location();
            user.setLocation(location);
            location.setUser(user);
            userRepo.save(user);
        }
        location.setLongitude(response.getLongitude());
        location.setLatitude(response.getLatitude());
        System.out.println("Saving the location");

        return locationRepo.save(location);
    }
}
