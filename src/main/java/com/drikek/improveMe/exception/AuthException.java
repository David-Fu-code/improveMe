package com.drikek.improveMe.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthException extends RuntimeException{
    private final int status;

    public AuthException(String message, int status) {
        super(message);
        this.status = status;
    }

}
