package com.strikezone.strikezone_backend.domain.comment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strikezone.strikezone_backend.domain.comment.dto.request.controller.CommentCreateRequest;
import com.strikezone.strikezone_backend.domain.comment.dto.request.controller.CommentUpdateRequest;
import com.strikezone.strikezone_backend.domain.comment.dto.response.CommentResponseDto;
import com.strikezone.strikezone_backend.domain.comment.service.CommentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CommentService commentService;

    @Test
    @DisplayName("댓글 생성 요청을 처리하여 정상적으로 생성한다")
    @WithMockUser(value = "testUser", roles = "USER")
    void createComment_success() throws Exception {
        // given: 컨트롤러 전용 DTO
        CommentCreateRequest request = CommentCreateRequest.builder()
                                                           .postId(1L)
                                                           .userId(1L)
                                                           .content("Test comment")
                                                           .build();

        CommentResponseDto serviceResponse = CommentResponseDto.builder()
                                                               .commentId(1L)
                                                               .postId(1L)
                                                               .userId(1L)
                                                               .content("Test comment")
                                                               .build();

        when(commentService.createComment(any())).thenReturn(serviceResponse);

        URI expectedLocation = URI.create("/api/comments" + serviceResponse.getCommentId());

        // when & then
        mockMvc.perform(post("/api/comments")
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isCreated())
               .andExpect(header().string("Location", expectedLocation.toString()))
               .andExpect(jsonPath("$.commentId", is(1)))
               .andExpect(jsonPath("$.postId", is(1)))
               .andExpect(jsonPath("$.userId", is(1)))
               .andExpect(jsonPath("$.content", is("Test comment")));
    }

    @Test
    @DisplayName("댓글 수정 요청을 처리하여 정상적으로 수정한다")
    @WithMockUser(value = "testUser", roles = "USER")
    void updateComment_success() throws Exception {
        // given
        Long commentId = 1L;
        CommentUpdateRequest request = CommentUpdateRequest.builder()
                                                           .userId(1L)
                                                           .content("Updated comment")
                                                           .build();

        CommentResponseDto serviceResponse = CommentResponseDto.builder()
                                                               .commentId(commentId)
                                                               .postId(1L)
                                                               .userId(1L)
                                                               .content("Updated comment")
                                                               .build();

        when(commentService.updateComment(any())).thenReturn(serviceResponse);

        // when & then
        mockMvc.perform(patch("/api/comments/{commentId}", commentId)
                       .contentType("application/json")
                       .content(objectMapper.writeValueAsString(request)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.commentId", is(1)))
               .andExpect(jsonPath("$.content", is("Updated comment")));
    }

    @Test
    @DisplayName("댓글 삭제 요청을 처리하여 정상적으로 삭제한다")
    @WithMockUser(value = "testUser", roles = "USER")
    void deleteComment_success() throws Exception {
        // given
        Long commentId = 1L;
        Long userId = 1L;

        mockMvc.perform(delete("/api/comments/{commentId}", commentId)
                       .param("userId", String.valueOf(userId)))
               .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("게시글의 댓글 목록 조회 요청을 처리하여 정상적으로 응답한다")
    @WithMockUser(value = "testUser", roles = "USER")
    void getCommentsByPost_success() throws Exception {
        // given
        Long postId = 1L;
        int page = 0;
        List<CommentResponseDto> serviceResponses = Arrays.asList(
                CommentResponseDto.builder().commentId(1L).postId(postId).userId(1L).content("Comment 1").build(),
                CommentResponseDto.builder().commentId(2L).postId(postId).userId(2L).content("Comment 2").build()
        );
        Page<CommentResponseDto> commentPage = new PageImpl<>(serviceResponses, PageRequest.of(page, 20), serviceResponses.size());

        when(commentService.getCommentsByPostWithPaging(postId, page)).thenReturn(commentPage);

        // when & then
        mockMvc.perform(get("/api/comments")
                       .param("postId", String.valueOf(postId))
                       .param("page", String.valueOf(page)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content", hasSize(2)))
               .andExpect(jsonPath("$.content[0].commentId", is(1)))
               .andExpect(jsonPath("$.content[0].content", is("Comment 1")))
               .andExpect(jsonPath("$.content[1].commentId", is(2)))
               .andExpect(jsonPath("$.content[1].content", is("Comment 2")));
    }

}
