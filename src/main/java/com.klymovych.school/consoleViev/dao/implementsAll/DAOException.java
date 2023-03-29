package com.klymovych.school.consoleViev.dao.implementsAll;

public class DAOException extends RuntimeException{
    public DAOException(){
        super();
    }

    public DAOException(String message, Throwable cause){
        super(message, cause);
    }

    public DAOException(String message){
        super(message);
    }
}
