package com.security.service.Controller;

import com.security.service.ApiResponse.ApiResponse;
import com.security.service.ApiResponse.ApiUtils;
import com.security.service.DTO.UserDto;
import com.security.service.Entity.User;
import com.security.service.Service.MailService;
import com.security.service.Service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController extends ApiUtils {
    @Autowired
    UserService userService;

    @GetMapping("view-all")
    public ResponseEntity<ApiResponse<List<User>>> get() {
        return getApiResponse(userService.get(), "List of users");
    }

    @PostMapping("add")
    public ResponseEntity<ApiResponse<UserDto>> add(@RequestBody User user) {
        return getApiResponse(userService.add(user), "User Added");
    }

    @GetMapping("view")
    public ResponseEntity<ApiResponse<UserDto>> view(HttpServletRequest request) throws MessagingException {
        UserDto dto = UserDto.entityToDto(userService.view(request.getAttribute("email").toString()));
        return getApiResponse(dto, "User View");
    }

    @PutMapping("update")
    public ResponseEntity<ApiResponse<UserDto>> update(@RequestBody UserDto userDto, HttpServletRequest request) {
        userDto.setEmail(request.getAttribute("email").toString());
        return getApiResponse(userService.update(userDto), "User Updated");
    }

    @DeleteMapping("delete")
    public ResponseEntity<ApiResponse<Void>> delete(HttpServletRequest request) {
        userService.delete(request.getAttribute("email").toString());
        return getApiResponse(ResponseEntity.noContent().build(), "User Deleted");
    }

    //logic will be that the user with the email will be able to monitor live location of the users in the list.
    //to do that, the user should be a member of Securellance
    @PostMapping("add-watch-live")
    public ResponseEntity<ApiResponse<List<String>>> addWatchLive(@RequestBody UserDto userDto, HttpServletRequest request){
        return getApiResponse(userService.addWatchLive(userDto,request.getAttribute("email").toString()),"List of users for live location monitoring");
    }
    @PutMapping("add-live-listeners")
    public ResponseEntity<ApiResponse<List<String>>> addLiveListeners(HttpServletRequest request,@RequestBody UserDto dto){
        return getApiResponse(userService.addLiveListeners(dto,request.getAttribute("email").toString()),"Live Listeners Added");
    }
    @GetMapping("email-search")
    public ResponseEntity<ApiResponse<List<String>>> searchEmail(@RequestParam("query") String search){
        return getApiResponse(userService.searchEmail(search),"Search List");
    }
}