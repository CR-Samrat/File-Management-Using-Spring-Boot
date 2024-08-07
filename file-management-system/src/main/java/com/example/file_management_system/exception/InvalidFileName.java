package com.example.file_management_system.exception;

public class InvalidFileName extends RuntimeException{
    
    public InvalidFileName(String file){
        super("Invalid File name with name "+file);
    }
}
