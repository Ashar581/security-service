package com.security.service.Controller;

import com.security.service.ApiResponse.ApiResponse;
import com.security.service.ApiResponse.ApiUtils;
import com.security.service.Jwt.JwtTokenGenerator;
import com.security.service.Model.LocationResponse;
import com.security.service.Service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/location")
public class LocationController extends ApiUtils {
    @Autowired
    LocationService locationService;
    @PutMapping("get-live")
    public ResponseEntity<ApiResponse<LocationResponse>> getLiveLocation(@RequestBody LocationResponse location, HttpServletRequest request){
        locationService.addLiveLocation(request.getAttribute("email").toString(),location);
        return getApiResponse(location,"Live LocationResponse");
    }
    @GetMapping("get-location")
    public ResponseEntity<ApiResponse<LocationResponse>> getLocation(HttpServletRequest request){
        //send the request, extract the token internally and get the allowed list.
        return getApiResponse(locationService.getLiveLocation(request.getHeader("Authorization").trim().replace("Bearer ","")),"Live Location");
    }
}
