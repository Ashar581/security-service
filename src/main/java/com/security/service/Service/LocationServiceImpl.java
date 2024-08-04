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
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User Not Found"));

        Location location = user.getLocation();

        if (Objects.isNull(location)) {
            location = new Location();
            location.setUser(user);
            user.setLocation(location);
        }

        location.setLatitude(response.getLatitude());
        location.setLongitude(response.getLongitude());

        userRepo.save(user);


        return location;
    }
    @Override
    public List<LocationResponse> getLiveLocation(String token) {
        //Fetch the list
        List<String> allowedUserView = jwtTokenExtractor.getAllowedUserList(token);
        //Logic for getting list of location of allowed users.
        List<LocationResponse> locationList = new ArrayList<>();
        for (String email:allowedUserView){
            User user = userRepo.findByEmail(email)
                    .orElse(null);
            if (Objects.nonNull(user) && Objects.nonNull(user.getLocation())) {
                LocationResponse location = new LocationResponse();
                location.setLongitude(user.getLocation().getLongitude());
                location.setLatitude(user.getLocation().getLatitude());
                location.setName(user.getFirstName()+" "+user.getLastName());
                location.setEmail(email);

                locationList.add(location);
            }
        }
        return locationList;
    }

    @Override
    public LocationResponse sendLiveLocation(String email) throws UserNotFoundException, LocationIdNotFoundException{
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        if (user.getLocation()==null) throw new LocationIdNotFoundException("There was no live location registered");
        LocationResponse response = new LocationResponse();
        response.setEmail(email);
        response.setName(user.getFirstName());
        response.setLatitude(user.getLocation().getLatitude());
        response.setLongitude(user.getLocation().getLongitude());

        return response;
    }
}
