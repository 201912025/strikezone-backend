package com.strikezone.strikezone_backend.domain.post.service;

import com.strikezone.strikezone_backend.domain.post.dto.response.PostResponseDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.post.repository.PostRepository;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글을 생성하면 성공적으로 PostResponseDTO가 반환되어야 한다")
    public void testCreatePost() {
        // Given
        PostRequestServiceDTO postRequestServiceDTO = new PostRequestServiceDTO("testUser", "testTeam", "Test Title", "Test Content");

        User user = User.builder()
                        .username("testUser")
                        .email("test@example.com")
                        .password("password")
                        .role("USER")
                        .build();

        Team team = Team.builder()
                        .name(TeamName.KIA)
                        .build();

        Post post = Post.builder().title("Test Title").content("Test Content").build();

        when(userService.getUserBySecurityUsername(postRequestServiceDTO.getUsername())).thenReturn(user);
        when(teamService.findByTeamName(postRequestServiceDTO.getTeamName())).thenReturn(team);
        when(postRepository.existsByTitle(postRequestServiceDTO.getTitle())).thenReturn(false);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        post.addTeam(team);
        post.addUser(user);

        // When
        PostResponseDTO postResponseDTO = postService.createPost(postRequestServiceDTO);

        // Then
        assertNotNull(postResponseDTO);
        assertEquals("Test Title", postResponseDTO.getTitle());
        verify(postRepository, times(1)).save(any(Post.class));
    }



    @Test
    @DisplayName("게시글 제목이 중복되면 BadRequestException이 발생해야 한다")
    public void testCreatePost_withDuplicatedTitle() {
        // Given
        PostRequestServiceDTO postRequestServiceDTO = new PostRequestServiceDTO("testUser", "testTeam", "Test Title", "Test Content");
        when(postRepository.existsByTitle(postRequestServiceDTO.getTitle())).thenReturn(true);

        // When / Then
        assertThrows(BadRequestException.class, () -> postService.createPost(postRequestServiceDTO), "Duplicated title should throw exception");
    }
}
