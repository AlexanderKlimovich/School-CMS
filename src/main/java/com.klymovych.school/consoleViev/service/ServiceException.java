package com.klymovych.school.consoleViev.service;

public class ServiceException extends RuntimeException{
    public ServiceException(){
        super();
    }

    public ServiceException(String message, Throwable cause){
        super(message, cause);
    }

    public ServiceException(String message){
        super(message);
    }
}
