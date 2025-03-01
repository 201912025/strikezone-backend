package com.strikezone.strikezone_backend.domain.user.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JoinServiceDTO {

    private String username;
    private String password;
    private String email;
    private String role;
    private String gender;
    private String birthDay;
    private String bio;
    private String teamName;

    @Builder
    public JoinServiceDTO(String username, String password, String email, String role, String gender, String birthDay, String bio, String teamName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.gender = gender;
        this.birthDay = birthDay;
        this.bio = bio;
        this.teamName = teamName;
    }
}
