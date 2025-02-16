package com.strikezone.strikezone_backend.domain.user.service;

import com.strikezone.strikezone_backend.domain.user.dto.response.UserResponseDTO;
import com.strikezone.strikezone_backend.domain.user.dto.service.JoinServiceDTO;
import com.strikezone.strikezone_backend.domain.user.dto.service.UpdateUserServiceDTO;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.exception.UserExceptionType;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void joinUser(JoinServiceDTO joinServiceDTO) {

        checkUsernameValid(joinServiceDTO.getUsername());

        User user = User.builder()
                .username(joinServiceDTO.getUsername())
                .password((bCryptPasswordEncoder.encode(joinServiceDTO.getPassword())))
                .role("USER")
                .bio(joinServiceDTO.getBio())
                .email(joinServiceDTO.getEmail())
                .team(joinServiceDTO.getTeam())
                .build();

        userRepository.save(user);
    }

    private void checkUsernameValid(String userName) {
        if (userName == null || userName.isEmpty()) {
            throw new BadRequestException(UserExceptionType.INVALID_NICKNAME);
        }

        if (userRepository.existsByUsername(userName)) {
            throw new BadRequestException(UserExceptionType.DUPLICATED_NICKNAME);
        }
    }

    public UserResponseDTO getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(UserExceptionType.NOT_FOUND_USER));

        UserResponseDTO userResponseDTO = UserResponseDTO.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .gender(user.getGender())
                .birthDay((user.getBirthDay()))
                .bio(user.getBio())
                .teamName(user.getTeam().toString())
                .build();

        return userResponseDTO;
    }

    @Transactional
    public void updateUser(UpdateUserServiceDTO updateUserServiceDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->new NotFoundException(UserExceptionType.NOT_FOUND_USER));

        user.setEmail(updateUserServiceDTO.getBio());
        user.setTeam(updateUserServiceDTO.getTeam());
        user.setBio(updateUserServiceDTO.getEmail());

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(UserExceptionType.NOT_FOUND_USER));

        userRepository.delete(user);
    }

    public User getUserBySecurity() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException(UserExceptionType.NOT_FOUND_USER));

        return user;
    }

}
