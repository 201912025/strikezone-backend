package com.strikezone.strikezone_backend.domain.user.dto.controller;

import com.strikezone.strikezone_backend.domain.user.dto.service.JoinServiceDTO;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;


@Getter
public class JoinControllerDTO {

    @NotBlank(message = "Username은 필수입니다.")
    @Size(min = 3, max = 20, message = "Username은 3자 이상 20자 이하이어야 합니다.")
    private String username;

    @NotBlank(message = "Password는 필수입니다.")
    @Size(min = 8, max = 20, message = "Password는 8자 이상 20자 이하이어야 합니다.")
    private String password;

    @NotBlank(message = "Email은 필수입니다.")
    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank(message = "성별은 필수입니다.")
    private String gender;

    @NotBlank(message = "생일은 필수입니다.")
    private String birthDay;

    @NotBlank(message = "소개글은 필수입니다.")
    private String bio;

    private String teamName;

    @Builder
    public JoinControllerDTO(String username, String password, String email, String gender, String birthDay, String bio, String teamName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.birthDay = birthDay;
        this.bio = bio;
        this.teamName = teamName;
    }

    public JoinServiceDTO toServiceDTO() {
        return JoinServiceDTO.builder()
                .username(this.username)
                .password(this.password)
                .email(this.email)
                .gender(this.gender)
                .birthDay(this.birthDay)
                .bio(this.bio)
                .teamName(this.teamName)
                .build();
    }
}
