package com.security.service.Controller;

import com.security.service.ApiResponse.ApiResponse;
import com.security.service.ApiResponse.ApiUtils;
import com.security.service.Model.LoginRequest;
import com.security.service.Model.LoginResponse;
import com.security.service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthenticationController extends ApiUtils {

    @Autowired
    UserService userService;
    @PostMapping("signin")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request){
        return getApiResponse(userService.authenticate(request),"User Logged In Successfully");
    }
}
