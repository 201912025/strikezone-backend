package com.strikezone.strikezone_backend.domain.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strikezone.strikezone_backend.domain.user.dto.controller.JoinControllerDTO;
import com.strikezone.strikezone_backend.domain.user.dto.controller.UpdateUserControllerDTO;
import com.strikezone.strikezone_backend.domain.user.dto.response.UserResponseDTO;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc 자동설정 및 시큐리티 빈 띄움
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private JoinControllerDTO joinControllerDTO;
    private UpdateUserControllerDTO updateUserControllerDTO;
    private UserResponseDTO userResponseDTO;

    @BeforeEach
    void setUp() {
        joinControllerDTO = JoinControllerDTO.builder()
                .username("johndoe")
                .password("password123")
                .email("johndoe@example.com")
                .bio("Developer")
                .teamName("KIA")
                .build();

        updateUserControllerDTO = UpdateUserControllerDTO.builder()
                .email("updated.email@example.com")
                .bio("Updated Bio")
                .team(null)
                .build();

        userResponseDTO = UserResponseDTO.builder()
                                         .username("johndoe")
                                         .email("johndoe@example.com")
                                         .role("USER")
                                         .gender(null)
                                         .birthDay(null)
                                         .bio("Developer")
                                         .teamName(null)  // 예시로 null로 설정
                                         .build();
    }

    @Test
    @DisplayName("회원 가입이 성공하는 테스트")
    void joinUser() throws Exception {
        doNothing().when(userService).joinUser(any());

        mockMvc.perform(post("/api/users/join")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(joinControllerDTO)))
                .andExpect(status().isCreated());

        verify(userService, times(1)).joinUser(any());
    }

    @Test
    @DisplayName("내 사용자 정보를 조회할 때 성공하는 테스트")
    @WithMockUser(username = "johndoe", roles = "USER")
    void getMyUserInfo() throws Exception {
        when(userService.getUserByUsername("johndoe")).thenReturn(userResponseDTO);

        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.username").value("johndoe"))
               .andExpect(jsonPath("$.teamName").value((Object) null))
               .andExpect(jsonPath("$.email").value("johndoe@example.com"))
               .andExpect(jsonPath("$.bio").value("Developer"));

        verify(userService, times(1)).getUserByUsername("johndoe");
    }

    @Test
    @DisplayName("회원 정보 업데이트가 성공하는 테스트")
    @WithMockUser(username = "johndoe", roles = "USER")  // 인증된 사용자 설정
    void updateUser() throws Exception {
        doNothing().when(userService).updateUser(any(), any());

        mockMvc.perform(post("/api/users/update")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateUserControllerDTO)))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).updateUser(any(), eq("johndoe"));
    }

    @Test
    @DisplayName("사용자 삭제가 성공하는 테스트")
    @WithMockUser(username = "johndoe", roles = "USER")  // 인증된 사용자 설정
    void deleteUser() throws Exception {
        doNothing().when(userService).deleteUser(any());

        mockMvc.perform(post("/api/users/delete"))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser("johndoe");
    }
}
