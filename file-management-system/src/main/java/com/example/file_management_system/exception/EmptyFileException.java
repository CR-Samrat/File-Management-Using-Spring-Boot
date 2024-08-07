package com.example.file_management_system.exception;

public class EmptyFileException extends RuntimeException{
    public EmptyFileException(){
        super("File can't be empty");
    }
}
