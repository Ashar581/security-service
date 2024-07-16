package com.security.service.Controller;

import com.security.service.ApiResponse.ApiResponse;
import com.security.service.ApiResponse.ApiUtils;
import com.security.service.Entity.File;
import com.security.service.Service.FileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/files")
public class FileController extends ApiUtils {
    @Autowired
    FileService fileService;
    @GetMapping("view-all")
    public ResponseEntity<ApiResponse<List<File>>> view(HttpServletRequest request){
        return getApiResponse(fileService.get(request.getAttribute("email").toString()),"All Stored Files");
    }
    @PostMapping("add")
    public ResponseEntity<ApiResponse<File>> add(@RequestParam("file")MultipartFile file, HttpServletRequest request){
        return getApiResponse(fileService.add(file,request.getAttribute("email").toString()),"File Added");
    }
    @GetMapping("view/{file-id}")
    public ResponseEntity<ApiResponse<File>> viewById(@PathVariable("file-id")Long fileID){
        return getApiResponse(fileService.view(fileID),"View File");
    }
}
