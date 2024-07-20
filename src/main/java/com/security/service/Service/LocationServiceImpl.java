package com.security.service.Service;

import com.security.service.Entity.Location;
import com.security.service.Entity.User;
import com.security.service.Exceptions.LocationIdNotFoundException;
import com.security.service.Exceptions.UserNotFoundException;
import com.security.service.Jwt.JwtTokenExtractor;
import com.security.service.Model.LocationResponse;
import com.security.service.Repository.LocationRepo;
import com.security.service.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class LocationServiceImpl implements LocationService{
    @Autowired
    UserRepo userRepo;
    @Autowired
    LocationRepo locationRepo;

    @Autowired
    JwtTokenExtractor jwtTokenExtractor;

    @Override
    public Location addLiveLocation(String email, LocationResponse response) {
        // Find user by email or throw an exception if the user is not found
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        // Get the user's current location
        Location location = user.getLocation();
        System.out.println("Got the user location...");

        // If the location is null, create a new Location object
        if (Objects.isNull(location)) {
            System.out.println("Creating a new location for the user...");
            location = new Location();
            location.setUser(user);  // Associate the new location with the user
            user.setLocation(location);  // Associate the user with the new location
        }

        // Update the location's latitude and longitude
        System.out.println("Updating location coordinates...");
        location.setLatitude(response.getLatitude());
        location.setLongitude(response.getLongitude());

        // Save the updated user (which cascades and saves the location due to CascadeType.ALL)
        userRepo.save(user);

        System.out.println("Location updated and user saved.");

        return location;
    }
    @Override
    public List<LocationResponse> getLiveLocation(String token) {
        //Fetch the list
        List<String> allowedUserView = jwtTokenExtractor.getAllowedUserList(token);
        System.out.println("Allowed User List"+allowedUserView);
        //Logic for getting list of location of allowed users.
        List<LocationResponse> locationList = new ArrayList<>();
        for (String email:allowedUserView){
            User user = userRepo.findByEmail(email).get();
            if (Objects.nonNull(user) && Objects.nonNull(user.getLocation())) {
                LocationResponse location = new LocationResponse();
                location.setLongitude(user.getLocation().getLongitude());
                location.setLatitude(user.getLocation().getLatitude());
                location.setName(user.getFirstName()+" "+user.getLastName());
                location.setEmail(email);

                locationList.add(location);
            }
//            if (Objects.nonNull(user) && Objects.nonNull(user.getLocation())) locationList.add(user.getLocation());
        }
        System.out.println("View Allowed User's Location List: "+locationList);
        return locationList;
    }
}
