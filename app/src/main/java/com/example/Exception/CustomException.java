package com.example.Exception;

/**
 * Created by JongHunLee on 2015-05-22.
 */
public class CustomException extends Exception {

    private String msg;

    public CustomException(String msg) {
        this.msg = msg;
    }

    @Override
    public String getMessage() {
        return msg;
    }
}
