package com.security.service.Service;

import com.security.service.Entity.Location;
import com.security.service.Model.LocationResponse;
import jakarta.transaction.Transactional;

public interface LocationService {
    @Transactional
    Location addLiveLocation(String email, LocationResponse location);
}
