package com.security.service.Controller;

import com.security.service.ApiResponse.ApiResponse;
import com.security.service.ApiResponse.ApiUtils;
import com.security.service.DTO.UserDto;
import com.security.service.Entity.SOS;
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

    //in future we can change list of string to List of users (a self mapping) and also keep additional fields such as
    // whether or not I allow that user to watch my live location for that time maybe and so on and so forth.
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
    //we do not have any separate SOS controller because we are string only the emails of the
    // added sos contacts and not the whole data and also we are currently keeping one SOS message for all users.
    // in coming future it is possible to change the mappings for SOS as well as allowedUsers.
    // we might as well have separate messages for contacts.
    @PutMapping("add-sos")
    public ResponseEntity<ApiResponse<SOS>> addSos(HttpServletRequest request, @RequestBody UserDto dto){
        return getApiResponse(userService.addSos(dto,request.getAttribute("email").toString()),"SOS was added");
    }
}