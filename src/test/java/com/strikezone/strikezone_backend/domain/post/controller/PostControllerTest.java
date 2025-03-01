package com.strikezone.strikezone_backend.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strikezone.strikezone_backend.domain.post.dto.controller.PostRequestDTO;
import com.strikezone.strikezone_backend.domain.post.dto.controller.PostUpdateRequestDTO;
import com.strikezone.strikezone_backend.domain.post.dto.response.PostResponseDTO;
import com.strikezone.strikezone_backend.domain.post.exception.PostExceptionType;
import com.strikezone.strikezone_backend.domain.post.service.PostService;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc  // MockMvc 자동설정 및 시큐리티 빈 띄움
class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PostService postService;

    @Test
    @DisplayName("게시글을 생성하면 성공적으로 PostResponseDTO가 반환되어야 한다")
    @WithMockUser(value = "testUser", roles = "USER")
    void testCreatePost() throws Exception {
        // Given
        PostRequestDTO postRequestDTO = PostRequestDTO.builder()
                                                      .teamName("KIA")
                                                      .title("Test Title")
                                                      .content("Test Content")
                                                      .build();

        PostResponseDTO postResponseDTO = PostResponseDTO.builder()
                                                         .postId(1L)
                                                         .title("Test Title")
                                                         .content("Test Content")
                                                         .teamName("testTeam")
                                                         .username("testUser")
                                                         .views(0)
                                                         .likes(0)
                                                         .build();

        when(postService.createPost(any())).thenReturn(postResponseDTO);

        // When / Then
        mockMvc.perform(post("/api/posts")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(postRequestDTO)))
               .andExpect(status().isCreated())  // 201 Created
               .andExpect(header().string("Location", "/api/posts/1"))
               .andExpect(jsonPath("$.postId").value(1L))
               .andExpect(jsonPath("$.title").value("Test Title"))
               .andExpect(jsonPath("$.content").value("Test Content"));

        verify(postService, times(1)).createPost(any());
    }

    @Test
    @DisplayName("게시글 제목이 중복되면 BadRequestException이 발생해야 한다")
    @WithMockUser(value = "testUser", roles = "USER")
    void testCreatePost_withDuplicatedTitle() throws Exception {
        // Given
        PostRequestDTO postRequestDTO = PostRequestDTO.builder()
                                                      .teamName("KIA")
                                                      .title("Test Title")
                                                      .content("Test Content")
                                                      .build();

        when(postService.createPost(any())).thenThrow(new BadRequestException(PostExceptionType.DUPLICATED_TITLE));

        // When / Then
        mockMvc.perform(post("/api/posts")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(postRequestDTO)))
               .andExpect(status().isBadRequest());  // 400 Bad Request

        verify(postService, times(1)).createPost(any());
    }

    @Test
    @DisplayName("모든 게시글을 조회할 수 있어야 한다")
    @WithMockUser(value = "testUser", roles = "USER")
    void testGetPosts() throws Exception {
        // Given
        PostResponseDTO postResponseDTO1 = PostResponseDTO.builder()
                                                          .postId(1L)
                                                          .title("Post 1")
                                                          .content("Content 1")
                                                          .teamName("testTeam")
                                                          .username("testUser")
                                                          .views(0)
                                                          .likes(0)
                                                          .build();

        PostResponseDTO postResponseDTO2 = PostResponseDTO.builder()
                                                          .postId(2L)
                                                          .title("Post 2")
                                                          .content("Content 2")
                                                          .teamName("testTeam")
                                                          .username("testUser")
                                                          .views(0)
                                                          .likes(0)
                                                          .build();

        when(postService.getPosts()).thenReturn(List.of(postResponseDTO1, postResponseDTO2));

        // When / Then
        mockMvc.perform(get("/api/posts"))
               .andExpect(status().isOk())  // 200 OK
               .andExpect(jsonPath("$[0].postId").value(1L))
               .andExpect(jsonPath("$[0].title").value("Post 1"))
               .andExpect(jsonPath("$[1].postId").value(2L))
               .andExpect(jsonPath("$[1].title").value("Post 2"));

        verify(postService, times(1)).getPosts();
    }

    @Test
    @DisplayName("게시글 ID로 조회하면 PostResponseDTO를 반환해야 한다")
    @WithMockUser(value = "testUser", roles = "USER")
    void testGetPostById() throws Exception {
        // Given
        PostResponseDTO postResponseDTO = PostResponseDTO.builder()
                                                         .postId(1L)
                                                         .title("Test Title")
                                                         .content("Test Content")
                                                         .teamName("testTeam")
                                                         .username("testUser")
                                                         .views(0)
                                                         .likes(0)
                                                         .build();

        when(postService.getPostById(1L)).thenReturn(postResponseDTO);

        // When / Then
        mockMvc.perform(get("/api/posts/{postId}", 1L))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.postId").value(1L))
               .andExpect(jsonPath("$.title").value("Test Title"))
               .andExpect(jsonPath("$.content").value("Test Content"));

        verify(postService, times(1)).getPostById(1L);
    }

    @Test
    @DisplayName("게시글 업데이트 요청(PATCH) 시 업데이트된 PostResponseDTO를 반환해야 한다")
    @WithMockUser(value = "testUser", roles = "USER")
    void testUpdatePost() throws Exception {
        // Given
        Long postId = 1L;
        PostUpdateRequestDTO updateRequestDTO = PostUpdateRequestDTO.builder()
                                                                    .title("Updated Title")
                                                                    .content("Updated Content")
                                                                    .build();

        PostResponseDTO postResponseDTO = PostResponseDTO.builder()
                                                         .postId(postId)
                                                         .title("Updated Title")
                                                         .content("Updated Content")
                                                         .teamName("testTeam")
                                                         .username("testUser")
                                                         .views(0)
                                                         .likes(0)
                                                         .build();

        when(postService.updatePost(any())).thenReturn(postResponseDTO);

        // When / Then
        mockMvc.perform(patch("/api/posts/{postId}", postId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(updateRequestDTO)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.postId").value(postId))
               .andExpect(jsonPath("$.title").value("Updated Title"))
               .andExpect(jsonPath("$.content").value("Updated Content"));

        verify(postService, times(1)).updatePost(any());
    }

}
