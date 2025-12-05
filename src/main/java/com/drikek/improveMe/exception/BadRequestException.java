package com.drikek.improveMe.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BadRequestException extends RuntimeException{

    private final int status;

    public BadRequestException(String message, int status) {
        super(message);
        this.status = status;
    }
}
