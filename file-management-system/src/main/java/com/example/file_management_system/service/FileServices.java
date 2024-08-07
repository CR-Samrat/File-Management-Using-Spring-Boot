package com.example.file_management_system.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.file_management_system.exception.InvalidFileName;
import com.example.file_management_system.exception.EmptyFileException;
import com.example.file_management_system.exception.FileNotFoundException;

@Service
public class FileServices {
    
    private String STORAGE_PATH = "storage";


    public Boolean saveFile(MultipartFile file){
        
        // check if the input file is null
        if(file == null){
            throw new EmptyFileException();
        }

        // creating an empty file with the name of the input file
        var targetFile = new File(STORAGE_PATH+File.separator+file.getOriginalFilename());

        // some hacker can try to create an addition directory or can go back to previous directory by giving file name as // or ..
        // so we need to check if the new file i.e. created is in the same folder as "STORAGE_PATH" or not
        if(! targetFile.getParent().equals(STORAGE_PATH)){
            throw new InvalidFileName(file.getOriginalFilename());
        }

        // finally get the input stream and paste into the newly created file
        try {
            Files.copy(file.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public String[] getFiles(){
        var directory = new File(STORAGE_PATH);

        return directory.list();
    }

    public File downloadFile(String fileName) throws Exception{
        Boolean isPresent = Arrays.stream(this.getFiles())
                                    .toList().contains(fileName);

        if(isPresent){
            var file = new File(STORAGE_PATH + File.separator + fileName);

            if(file.getParent().equals(STORAGE_PATH)){
                return file;
            }else{
                throw new InvalidFileName(fileName);
            }
        }else{
            throw new FileNotFoundException(fileName);
        }
    }
}
