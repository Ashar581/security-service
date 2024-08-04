package com.security.service.Service;

import com.security.service.Entity.File;
import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    File add(MultipartFile file, String email);
    List<File> get(String email);
    File view(Long fileId);
    @Transactional
    void delete(Long fileId);
    List<String> search(String search);
}
