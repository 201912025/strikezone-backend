package com.strikezone.strikezone_backend.domain.user.controller;

import com.strikezone.strikezone_backend.domain.user.dto.controller.JoinControllerDTO;
import com.strikezone.strikezone_backend.domain.user.dto.controller.UpdateUserControllerDTO;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public ResponseEntity<Void> joinUser(@Valid @RequestBody JoinControllerDTO joinControllerDTO) {
        userService.joinUser(joinControllerDTO.toServiceDTO());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<User> getMyUserInfo() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
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
