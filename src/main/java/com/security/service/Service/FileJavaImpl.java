package com.security.service.Service;

import com.security.service.Entity.File;
import com.security.service.Entity.User;
import com.security.service.Exceptions.FileExceptionHandler;
import com.security.service.Exceptions.UserNotFoundException;
import com.security.service.Repository.FileRepo;
import com.security.service.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
public class FileJavaImpl implements FileService{
    @Autowired
    UserRepo userRepo;
    @Autowired
    FileRepo fileRepo;
    @Override
    public File add(MultipartFile file, String email) throws UserNotFoundException,FileExceptionHandler {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        String fileName = file.getOriginalFilename();

        try{
            File store = new File();
            store.setFileName(fileName);
            store.setData(file.getBytes());
            store.setFileType(file.getContentType());
            store.setUser(user);

            return fileRepo.save(store);
        }catch(Exception e){
            throw new FileExceptionHandler("File cannot be stored.Please Try Again");
        }
    }

    @Override
    public List<File> get(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));
        return fileRepo.findAllById(user.getUserID());
    }

    @Override
    public File view(Long fileId) throws FileExceptionHandler{
        File file = fileRepo.findById(fileId)
                .orElseThrow(()->new FileExceptionHandler("File Not Found"));
        return file;
    }
}
