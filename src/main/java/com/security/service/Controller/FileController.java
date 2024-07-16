package com.security.service.Controller;

import com.security.service.ApiResponse.ApiResponse;
import com.security.service.ApiResponse.ApiUtils;
import com.security.service.Entity.File;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public class FileController extends ApiUtils {
    @GetMapping("view-all")
    public ResponseEntity<ApiResponse<List<File>>> view(){
        return getApiResponse(null,"All Stored Files");
    }
}
