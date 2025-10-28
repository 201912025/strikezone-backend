package com.strikezone.strikezone_backend.domain.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strikezone.strikezone_backend.domain.post.dto.controller.PostRequestDTO;
import com.strikezone.strikezone_backend.domain.post.dto.controller.PostUpdateRequestDTO;
import com.strikezone.strikezone_backend.domain.post.dto.response.PostResponseDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostDeleteRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.exception.PostExceptionType;
import com.strikezone.strikezone_backend.domain.post.service.PostService;
import com.strikezone.strikezone_backend.domain.team.exception.TeamExceptionType;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
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

    @Test
    @WithMockUser(value = "testUser", roles = "USER")
    @DisplayName("좋아요 증가 컨트롤러 테스트: 204 No Content 반환")
    public void testIncrementLikesEndpoint() throws Exception {
        Long postId = 1L;
        doNothing().when(postService).incrementLikes(postId);

        mockMvc.perform(patch("/api/posts/{postId}/like", postId)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isNoContent());

        verify(postService, Mockito.times(1)).incrementLikes(postId);
    }

    @Test
    @DisplayName("getPostsPaged: 페이징 조회 엔드포인트 테스트")
    @WithMockUser(value = "testUser", roles = "USER")
    void testGetPostsPaged() throws Exception {
        Pageable pg = PageRequest.of(1, 10);
        Page<PostResponseDTO> pageDto = new PageImpl<>(
                List.of(PostResponseDTO.builder().postId(1L).title("A").content("B").teamName("T").username("u").views(0).likes(0).build()),
                pg, 1
        );
        when(postService.getPosts(1)).thenReturn(pageDto);

        mockMvc.perform(get("/api/posts/paged").param("page", "1"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].title").value("A"));

        verify(postService, times(1)).getPosts(1);
    }

    @Test
    @DisplayName("searchPosts: 키워드 검색 엔드포인트 테스트")
    @WithMockUser(value = "testUser", roles = "USER")
    void testSearchPostsEndpoint() throws Exception {
        String kw = "key";
        String st = "all";
        Pageable pg = PageRequest.of(0, 20);
        Page<PostResponseDTO> pageDto = new PageImpl<>(
                List.of(PostResponseDTO.builder().postId(2L).title("X").content("Y").teamName("T").username("u").views(0).likes(0).build()),
                pg, 1
        );
        when(postService.searchPosts(kw, st, pg)).thenReturn(pageDto);

        mockMvc.perform(get("/api/posts/search")
                       .param("keyword", kw)
                       .param("searchType", st)
                       .param("page", "0")
                       .param("size", "20"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].title").value("X"));

        verify(postService, times(1)).searchPosts(kw, st, pg);
    }

    @Test
    @DisplayName("searchPostsByTeam: 정상 팀 검색 엔드포인트 테스트")
    @WithMockUser(value="testUser", roles="USER")
    void testSearchPostsByTeamSuccess() throws Exception {
        String team = "KIA";
        Pageable pg = PageRequest.of(0, 20);
        Page<PostResponseDTO> pageDto = new PageImpl<>(
                List.of(PostResponseDTO.builder().postId(3L).title("Z").content("W").teamName(team).username("u").views(0).likes(0).build()),
                pg, 1
        );
        when(postService.searchPostsByTeam(team, pg)).thenReturn(pageDto);

        mockMvc.perform(get("/api/posts/team")
                       .param("teamName", team)
                       .param("page", "0")
                       .param("size", "20"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].teamName").value(team));

        verify(postService, times(1)).searchPostsByTeam(team, pg);
    }

    @Test
    @DisplayName("searchPostsByTeam: 잘못된 팀 이름 예외")
    @WithMockUser(value = "testUser", roles = "USER")
    void testSearchPostsByTeamInvalid() throws Exception {
        String bad = "INVALID";
        when(postService.searchPostsByTeam(eq(bad), any(Pageable.class)))
                .thenThrow(new BadRequestException(TeamExceptionType.INVALID_TEAM_NAME));

        mockMvc.perform(get("/api/posts/team").param("teamName", bad))
               .andExpect(status().isBadRequest());

        verify(postService, times(1)).searchPostsByTeam(eq(bad), any(Pageable.class));
    }

    @Test
    @DisplayName("deletePost: 정상 삭제 엔드포인트 테스트")
    @WithMockUser(value = "testUser", roles = "USER")
    void testDeletePostSuccess() throws Exception {
        doNothing().when(postService).deletePost(any(PostDeleteRequestServiceDTO.class));

        mockMvc.perform(delete("/api/posts/{postId}", 5L))
               .andExpect(status().isNoContent());

        verify(postService, times(1)).deletePost(any(PostDeleteRequestServiceDTO.class));
    }

    @Test
    @DisplayName("deletePost: 없는 게시글 삭제 시 예외")
    @WithMockUser(value = "testUser", roles = "USER")
    void testDeletePostNotFound() throws Exception {
        // deletePost가 호출될 때 BadRequestException이 던져지도록 stub 설정
        doThrow(new BadRequestException(PostExceptionType.NOT_FOUND_POST))
                .when(postService)
                .deletePost(any(PostDeleteRequestServiceDTO.class));

        mockMvc.perform(delete("/api/posts/{postId}", 123L))
               .andExpect(status().isBadRequest());

        verify(postService, times(1)).deletePost(any(PostDeleteRequestServiceDTO.class));
    }

    @Test
    @DisplayName("getPopularPosts: 인기 글 조회 엔드포인트 테스트")
    @WithMockUser(value = "testUser", roles = "USER")
    void testGetPopularPosts() throws Exception {
        List<PostResponseDTO> list = List.of(
                PostResponseDTO.builder().postId(9L).title("P").content("Q").teamName("T").username("u").views(0).likes(0).build()
        );
        when(postService.getPopularPosts()).thenReturn(list);

        mockMvc.perform(get("/api/posts/popular"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].postId").value(9L));

        verify(postService, times(1)).getPopularPosts();
    }
}
