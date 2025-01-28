package com.strikezone.strikezone_backend.domain.player.dto.controller;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatePlayerRequestDTO {

    private String name;
    private String teamName;
    private String position;
    private int number;

    @Builder
    public CreatePlayerRequestDTO(String name, String teamName, String position, int number) {
        this.name = name;
        this.teamName = teamName;
        this.position = position;
        this.number = number;
    }
}
