package com.strikezone.strikezone_backend.domain.post.service;

import com.strikezone.strikezone_backend.domain.post.dto.response.PostResponseDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostUpdateRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.post.exception.PostExceptionType;
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

import java.util.Optional;

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

    @Test
    @DisplayName("게시글을 정상적으로 수정하면 업데이트된 PostResponseDTO가 반환되어야 한다")
    public void testUpdatePost_Success() {
        // Given
        Long postId = 1L;
        Post post = spy(Post.builder().title("Same Title").content("Same Content").build());

        // User 객체를 spy로 생성하여 getUserId()를 Stub 처리
        User user = spy(User.builder().username("testUser").build());
        doReturn(1L).when(user).getUserId();

        PostUpdateRequestServiceDTO updateDTO = new PostUpdateRequestServiceDTO(postId, "testUser", "New Title", "New Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername(updateDTO.getUsername())).thenReturn(user);

        // post.getUser()가 user를 반환하도록 Stub
        doReturn(user).when(post).getUser();

        // When
        PostResponseDTO updatedPostDTO = postService.updatePost(updateDTO);

        // Then
        assertNotNull(updatedPostDTO);
        assertEquals("New Title", updatedPostDTO.getTitle());
        assertEquals("New Content", updatedPostDTO.getContent());
    }


    @Test
    @DisplayName("게시글을 수정할 때 작성자가 아니면 BadRequestException이 발생해야 한다")
    public void testUpdatePost_UnauthorizedUser() {
        // Given
        Long postId = 1L;
        Post post = spy(Post.builder().title("Same Title").content("Same Content").build());

        // User 엔티티: 기본키는 자동 생성이지만, 테스트에서는 Stub 처리로 ID 반환하도록 설정
        User postOwner = spy(User.builder().username("ownerUser").build());
        User differentUser = spy(User.builder().username("otherUser").build());

        // Stub: getUserId()가 각각 원하는 값을 반환하도록 설정
        doReturn(1L).when(postOwner).getUserId();
        doReturn(2L).when(differentUser).getUserId();

        PostUpdateRequestServiceDTO updateDTO =
                new PostUpdateRequestServiceDTO(postId, "otherUser", "New Title", "New Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername(updateDTO.getUsername())).thenReturn(differentUser);

        // Stub post.getUser() to return postOwner
        doReturn(postOwner).when(post).getUser();

        // When / Then
        BadRequestException exception = assertThrows(BadRequestException.class,
                () -> postService.updatePost(updateDTO));

        assertEquals(PostExceptionType.UNAUTHORIZED_USER, exception.getExceptionType());
    }


    @Test
    @DisplayName("게시글 수정 시 제목과 내용이 동일하면 업데이트 없이 기존 데이터를 반환해야 한다")
    public void testUpdatePost_NoChanges() {
        // Given
        Long postId = 1L;
        Post post = spy(Post.builder().title("Same Title").content("Same Content").build());

        User user = spy(User.builder().username("testUser").build());

        // Stub: getUserId()가 null 대신 반환되도록 설정
        doReturn(1L).when(user).getUserId();

        PostUpdateRequestServiceDTO updateDTO =
                new PostUpdateRequestServiceDTO(postId, "testUser", "Same Title", "Same Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername(updateDTO.getUsername())).thenReturn(user);

        // Stub post.getUser() to return the same user
        doReturn(user).when(post).getUser();

        // When
        PostResponseDTO result = postService.updatePost(updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Same Title", result.getTitle());
        assertEquals("Same Content", result.getContent());
        verify(postRepository, never()).save(any(Post.class)); // 업데이트가 발생하지 않았음을 검증
    }

}
