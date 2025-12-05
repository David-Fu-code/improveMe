package com.drikek.improveMe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisteredRequest {
    private String email;
    private String password;
    private String displayName;
}
