package com.security.service.Service;

import com.security.service.Entity.Location;
import com.security.service.Model.LocationResponse;
import jakarta.transaction.Transactional;

import java.util.List;

public interface LocationService {
    @Transactional
    Location addLiveLocation(String email, LocationResponse location);

    List<LocationResponse> getLiveLocation(String token);

    LocationResponse sendLiveLocation(String email);
}
