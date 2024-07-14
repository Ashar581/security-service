package com.security.service.Controller;

import com.security.service.ApiResponse.ApiResponse;
import com.security.service.ApiResponse.ApiUtils;
import com.security.service.DTO.UserDto;
import com.security.service.Entity.User;
import com.security.service.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController extends ApiUtils {
    @Autowired
    UserService userService;
    @GetMapping("view")
    public ResponseEntity<ApiResponse<List<User>>> get(){
        return getApiResponse(userService.get(),"List of users");
    }
    @PostMapping("add")
    public ResponseEntity<ApiResponse<UserDto>> add(@RequestBody User user){
        return getApiResponse(userService.add(user),"User Added");
    }
}
