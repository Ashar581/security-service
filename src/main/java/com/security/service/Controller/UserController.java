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

import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController extends ApiUtils {
    @Autowired
    UserService userService;
    @GetMapping("view-all")
    public ResponseEntity<ApiResponse<List<User>>> get(){
        return getApiResponse(userService.get(),"List of users");
    }
    @PostMapping("add")
    public ResponseEntity<ApiResponse<UserDto>> add(@RequestBody User user){
        return getApiResponse(userService.add(user),"User Added");
    }
    @GetMapping("view")
    public ResponseEntity<ApiResponse<UserDto>> view(HttpServletRequest request) throws MessagingException {
        UserDto dto = UserDto.entityToDto(userService.view(request.getAttribute("email").toString()));
        return getApiResponse(dto,"User View");
    }

    @PutMapping("update")
    public ResponseEntity<ApiResponse<UserDto>> update(@RequestBody UserDto userDto, HttpServletRequest request){
        userDto.setEmail(request.getAttribute("email").toString());
        return getApiResponse(userService.update(userDto),"User Updated");
    }

    @DeleteMapping("delete")
    public ResponseEntity<ApiResponse<Void>> delete(HttpServletRequest request){
        userService.delete(request.getAttribute("email").toString());
        return getApiResponse(ResponseEntity.noContent().build(),"User Deleted");
    }

    String html = "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Login Notification</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            font-family: Arial, sans-serif;\n" +
            "            background-color: #f4f4f4;\n" +
            "            color: #333;\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "        }\n" +
            "        .container {\n" +
            "            width: 100%;\n" +
            "            max-width: 600px;\n" +
            "            margin: 0 auto;\n" +
            "            background-color: #ffffff;\n" +
            "            padding: 20px;\n" +
            "            border-radius: 8px;\n" +
            "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
            "        }\n" +
            "        .header {\n" +
            "            background-color: #4CAF50;\n" +
            "            color: white;\n" +
            "            padding: 10px;\n" +
            "            border-top-left-radius: 8px;\n" +
            "            border-top-right-radius: 8px;\n" +
            "        }\n" +
            "        .content {\n" +
            "            margin: 20px 0;\n" +
            "        }\n" +
            "        .footer {\n" +
            "            text-align: center;\n" +
            "            font-size: 12px;\n" +
            "            color: #777;\n" +
            "            margin-top: 20px;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "    <div class=\"container\">\n" +
            "        <div class=\"header\">\n" +
            "            <h1>Login Notification</h1>\n" +
            "        </div>\n" +
            "        <div class=\"content\">\n" +
            "            <p>Dear [User's Name],</p>\n" +
            "            <p>We noticed that you just logged in to your account. If this was you, you can safely ignore this email.</p>\n" +
            "            <p>If you did not log in, please reset your password immediately and contact our support team.</p>\n" +
            "            <p>Thank you,<br>Your Company Name</p>\n" +
            "        </div>\n" +
            "        <div class=\"footer\">\n" +
            "            <p>&copy; 2024 Your Company Name. All rights reserved.</p>\n" +
            "        </div>\n" +
            "    </div>\n" +
            "</body>\n" +
            "</html>\n";
}
