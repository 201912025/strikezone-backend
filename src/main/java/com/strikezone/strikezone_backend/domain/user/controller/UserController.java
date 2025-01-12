package com.strikezone.strikezone_backend.domain.user.controller;

import com.strikezone.strikezone_backend.domain.user.dto.controller.JoinControllerDTO;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> joinUser(@Valid @RequestBody JoinControllerDTO joinControllerDTO) {
        userService.joinUser(joinControllerDTO.toServiceDTO());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
