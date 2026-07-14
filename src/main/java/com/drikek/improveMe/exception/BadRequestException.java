package com.drikek.improveMe.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@SuppressWarnings("unused")
public class BadRequestException extends RuntimeException {

    private final int status;

    public BadRequestException(String message, int status) {
        super(message);
        this.status = status;
    }
}
