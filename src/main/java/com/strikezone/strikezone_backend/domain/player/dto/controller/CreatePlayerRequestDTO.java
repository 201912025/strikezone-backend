package com.strikezone.strikezone_backend.domain.player.dto.controller;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatePlayerRequestDTO {

    @NotBlank(message = "이름은 필수 항목입니다.")
    private String name;

    @NotBlank(message = "팀 이름은 필수 항목입니다.")
    private String teamName;

    @NotBlank(message = "포지션은 필수 항목입니다.")
    private String position;

    @Min(value = 1, message = "등번호는 1 이상이어야 합니다.")
    @Max(value = 99, message = "등번호는 99 이하여야 합니다.")
    private int number;

    @Builder
    public CreatePlayerRequestDTO(String name, String teamName, String position, int number) {
        this.name = name;
        this.teamName = teamName;
        this.position = position;
        this.number = number;
    }
}
