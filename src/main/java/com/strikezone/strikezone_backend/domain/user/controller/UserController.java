package com.strikezone.strikezone_backend.domain.user.controller;

import com.strikezone.strikezone_backend.domain.user.dto.controller.JoinControllerDTO;
import com.strikezone.strikezone_backend.domain.user.dto.controller.UpdateUserControllerDTO;
import com.strikezone.strikezone_backend.domain.user.dto.response.UserResponseDTO;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinUser(@Valid @RequestBody JoinControllerDTO joinControllerDTO) {
        userService.joinUser(joinControllerDTO.toServiceDTO());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<UserResponseDTO> getMyUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponseDTO userResponseDTO = userService.getUserByUsername(username);
        return ResponseEntity.ok(userResponseDTO);
    }

    @PostMapping("/update")
    public ResponseEntity<Void> updateUser(@Valid @RequestBody UpdateUserControllerDTO updateUserControllerDTO) {
        String username  = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.updateUser(updateUserControllerDTO.toServiceDTO(), username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        userService.deleteUser(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
