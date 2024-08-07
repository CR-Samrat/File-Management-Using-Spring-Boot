package com.example.file_management_system.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.file_management_system.service.FileServices;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/file")
@CrossOrigin
public class FileController {

    @Autowired
    private FileServices services;
    
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file){

        if(this.services.saveFile(file)){
            return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Unsuccessful file submission", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllFiles(){
        List<String> files = Arrays.stream(this.services.getFiles())
                                .toList();

        return new ResponseEntity<>(files, HttpStatus.OK);
    }

    @GetMapping("/download/{file_name}")
    public ResponseEntity<?> downloadFile(@PathVariable("file_name") String fileName){
        var file = this.services.downloadFile(fileName);

        return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                            .contentLength(file.length())
                            .contentType(MediaType.APPLICATION_OCTET_STREAM)
                            .body(new FileSystemResource(file));
    }

    @GetMapping("/delete/{file_name}")
    public ResponseEntity<?> deleteFile(@PathVariable("file_name") String fileName){
        List<String> files = Arrays.stream(this.services.deleteFile(fileName)).toList();

        return ResponseEntity.ok()
                            .body(files);
    }
}
