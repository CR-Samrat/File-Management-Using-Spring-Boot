package com.example.file_management_system.exception;

public class FileNotFoundException extends RuntimeException{
    public FileNotFoundException(String file){
        super("File not found with name "+file);
    }
}
