package com.drikek.improveMe.dto;

import lombok.Data;

@Data
public class LogoutRequest {
    // frontend send actual refresh Token
    private String refreshToken;
}
