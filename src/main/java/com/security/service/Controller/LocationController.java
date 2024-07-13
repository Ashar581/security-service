package com.security.service.Controller;

import com.security.service.ApiResponse.ApiResponse;
import com.security.service.ApiResponse.ApiUtils;
import com.security.service.Model.LocationResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/location")
public class LocationController extends ApiUtils {
    @PostMapping("get-live")
    public ResponseEntity<ApiResponse<LocationResponse>> getLiveLocation(@RequestBody LocationResponse location){
        System.out.println("Longitude: "+location.getLongitude()+"\nLatitude: "+location.getLatitude());
        return getApiResponse(location,"Live LocationResponse");
    }
}
