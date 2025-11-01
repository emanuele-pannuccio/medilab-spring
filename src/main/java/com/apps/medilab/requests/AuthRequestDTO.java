package com.apps.medilab.requests;

import lombok.Data;

@Data
public class AuthRequestDTO {
    private String email;
    private String password;
}
