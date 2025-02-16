package com.strikezone.strikezone_backend.global.config.jwt.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

    private String username;
    private String password;
}

