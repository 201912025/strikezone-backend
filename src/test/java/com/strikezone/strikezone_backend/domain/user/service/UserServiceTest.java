package com.strikezone.strikezone_backend.domain.user.service;

import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.user.dto.service.JoinServiceDTO;
import com.strikezone.strikezone_backend.domain.user.dto.service.UpdateUserServiceDTO;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.exception.UserExceptionType;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private Team team;

    @InjectMocks
    private UserService userService;

    private JoinServiceDTO joinServiceDTO;
    private UpdateUserServiceDTO updateUserServiceDTO;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        joinServiceDTO = JoinServiceDTO.builder()
                .username("johndoe")
                .password("password123")
                .email("johndoe@example.com")
                .role("USER")
                .bio("Developer")
                .team(team)
                .build();

        updateUserServiceDTO = UpdateUserServiceDTO.builder()
                .email("johndoe@example.com")
                .bio("Updated Bio")
                .team(team)
                .build();

        user = User.builder()
                .username("johndoe")
                .password("encodedpassword")
                .email("johndoe@example.com")
                .role("USER")
                .bio("Developer")
                .team(team)
                .build();
    }

    @Test
    @DisplayName("회원 가입이 성공하는 테스트")
    void joinUserSuccess() {
        when(userRepository.existsByUsername(joinServiceDTO.getUsername())).thenReturn(false);
        when(bCryptPasswordEncoder.encode(joinServiceDTO.getPassword())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.joinUser(joinServiceDTO);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 사용자 이름으로 회원 가입을 시도했을 때 예외가 발생하는 테스트")
    void joinUserUsernameAlreadyExists() {
        when(userRepository.existsByUsername(joinServiceDTO.getUsername())).thenReturn(true);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.joinUser(joinServiceDTO);
        });

        assertEquals(UserExceptionType.DUPLICATED_NICKNAME, exception.getExceptionType());
    }

    @Test
    @DisplayName("사용자를 이름으로 조회했을 때 성공하는 테스트")
    void getUserByUsernameSuccess() {
        when(userRepository.findByUsername("johndoe")).thenReturn(java.util.Optional.of(user));

        User foundUser = userService.getUserByUsername("johndoe");

        assertNotNull(foundUser);
        assertEquals("johndoe", foundUser.getUsername());
    }

    @Test
    @DisplayName("존재하지 않는 사용자 이름으로 조회했을 때 예외가 발생하는 테스트")
    void getUserByUsernameUserNotFound() {
        when(userRepository.findByUsername("johndoe")).thenReturn(java.util.Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.getUserByUsername("johndoe");
        });

        assertEquals(UserExceptionType.NOT_FOUND_USER, exception.getExceptionType());
    }

    @Test
    @DisplayName("회원 정보 업데이트가 성공하는 테스트")
    void updateUserSuccess() {
        String username = "johndoe";

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UpdateUserServiceDTO updateUserServiceDTO = UpdateUserServiceDTO.builder()
                .email("updated.email@example.com")
                .bio("Updated bio")
                .team(team)
                .build();

        userService.updateUser(updateUserServiceDTO, username);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("회원 정보 업데이트 시 사용자 미존재 예외가 발생하는 테스트")
    void updateUserNotFoundException() {
        String username = "johndoe";

        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());

        UpdateUserServiceDTO updateUserServiceDTO = UpdateUserServiceDTO.builder()
                .email("updated.email@example.com")
                .bio("Updated bio")
                .team(team) // 팀 정보 업데이트
                .build();

        assertThrows(BadRequestException.class, () -> userService.updateUser(updateUserServiceDTO, username));
    }

    @Test
    @DisplayName("사용자 삭제가 성공하는 테스트")
    void deleteUserSuccess() {
        String username = "johndoe";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.of(user));

        userService.deleteUser(username);

        verify(userRepository, times(1)).delete(user);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 삭제 시 예외가 발생하는 테스트")
    void deleteUserUserNotFound() {
        String username = "johndoe";
        when(userRepository.findByUsername(username)).thenReturn(java.util.Optional.empty());

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            userService.deleteUser(username);
        });

        assertEquals(UserExceptionType.NOT_FOUND_USER, exception.getExceptionType());
    }
}
