package com.example.simple_spring_boot.storage;

public class StorageException extends RuntimeException {
    public StorageException(String message, Throwable cause){
        super(message, cause);
    }
    public StorageException(String message){
        super(message);
    }

}
