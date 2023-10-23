package com.seerbitTest.exception;

public class NoRecordFoundException extends RuntimeException{
    private static final long serialVersionUID = 1L;
    public NoRecordFoundException(String s){
        super(s);
    }
}
