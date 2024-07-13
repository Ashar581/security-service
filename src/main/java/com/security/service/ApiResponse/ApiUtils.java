package com.security.service.ApiResponse;

import org.springframework.http.ResponseEntity;

public class ApiUtils <T> {
    public ResponseEntity<ApiResponse<T>> getApiResponse(T data, String message){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setData(data);
        apiResponse.setStatus(true);
        apiResponse.setMessage(message);
        return ResponseEntity.ok(apiResponse);
    }
}
