package com.security.service.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> userNotFoundException(UserNotFoundException u){
        Map<String,Object> response = new HashMap<>();
        response.put("timespan", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        response.put("status",false);
        response.put("message", u.getMessage());

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler
    public ResponseEntity<Map<String,Object>> incorrectPasswordException(IncorrectPasswordException ipe){
        Map<String,Object> response = new HashMap<>();
        response.put("timespan", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        response.put("status",false);
        response.put("message", ipe.getMessage());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
