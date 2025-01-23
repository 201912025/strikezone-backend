package com.strikezone.strikezone_backend.domain.player.dto.service;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CreatePlayerRequestServiceDTO {

    private String name;

    private String teamName;

    private String position;

    private Integer number;

    @Builder
    public CreatePlayerRequestServiceDTO(String name, String teamName, String position, Integer number) {
        this.name = name;
        this.teamName = teamName;
        this.position = position;
        this.number = number;
    }
}
