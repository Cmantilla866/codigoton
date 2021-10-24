package com.example.CenaClientes.exception;

import lombok.Data;

/**
 * Custom Exception
 * */
@Data
public class CustomException extends  Exception{

    /** Serialize */
    private static final long serialVersionUID = 6365652257268547172L;
    private final String userMessage;
    private final int status;

    public CustomException(String developerMessage, String userMessage, int status) {
        super(developerMessage);
        this.userMessage = userMessage;
        this.status = status;
    }

}
