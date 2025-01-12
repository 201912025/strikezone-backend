package com.strikezone.strikezone_backend.domain.user.dto.controller;

import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.user.dto.service.UpdateUserServiceDTO;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UpdateUserControllerDTO {

    @Email(message = "유효한 이메일 형식이어야 합니다.")
    private String email;

    private String bio;

    private Team team;

    @Builder
    public UpdateUserControllerDTO(String email, String bio, Team team) {
        this.email = email;
        this.bio = bio;
        this.team = team;
    }

    public UpdateUserServiceDTO toServiceDTO() {
        return UpdateUserServiceDTO.builder()
                .email(this.email)
                .bio(this.bio)
                .team(this.team)
                .build();
    }
}
