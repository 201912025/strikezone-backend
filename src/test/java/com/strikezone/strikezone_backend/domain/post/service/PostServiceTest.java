package com.strikezone.strikezone_backend.domain.post.service;

import com.strikezone.strikezone_backend.domain.post.dto.response.PostResponseDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostDeleteRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostUpdateRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.post.exception.PostExceptionType;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.exception.TeamExceptionType;
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
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
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

        User user = spy(User.builder().username("testUser").build());
        doReturn(1L).when(user).getUserId();

        PostUpdateRequestServiceDTO updateDTO = new PostUpdateRequestServiceDTO(postId, "testUser", "New Title", "New Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername(updateDTO.getUsername())).thenReturn(user);

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

        User postOwner = spy(User.builder().username("ownerUser").build());
        User differentUser = spy(User.builder().username("otherUser").build());

        doReturn(1L).when(postOwner).getUserId();
        doReturn(2L).when(differentUser).getUserId();

        PostUpdateRequestServiceDTO updateDTO =
                new PostUpdateRequestServiceDTO(postId, "otherUser", "New Title", "New Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername(updateDTO.getUsername())).thenReturn(differentUser);

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

        doReturn(1L).when(user).getUserId();

        PostUpdateRequestServiceDTO updateDTO =
                new PostUpdateRequestServiceDTO(postId, "testUser", "Same Title", "Same Content");

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername(updateDTO.getUsername())).thenReturn(user);

        doReturn(user).when(post).getUser();

        // When
        PostResponseDTO result = postService.updatePost(updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("Same Title", result.getTitle());
        assertEquals("Same Content", result.getContent());
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 삭제가 성공적으로 이루어져야 한다")
    public void testDeletePost_Success() {
        // Given
        Long postId = 1L;
        String username = "testUser";
        PostDeleteRequestServiceDTO deleteDTO = new PostDeleteRequestServiceDTO(postId, username);

        User user = User.builder().username(username).build();
        ReflectionTestUtils.setField(user, "userId", 1L);

        Post post = Post.builder().title("Test Title").content("Test Content").build();
        post.addUser(user);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername(username)).thenReturn(user);

        // When
        postService.deletePost(deleteDTO);

        // Then
        verify(postRepository, times(1)).delete(post);
    }

    @Test
    @DisplayName("게시글 삭제 시 존재하지 않는 게시글이면 예외가 발생해야 한다")
    public void testDeletePost_PostNotFound() {
        // Given
        Long postId = 1L;
        String username = "testUser";
        PostDeleteRequestServiceDTO deleteDTO = new PostDeleteRequestServiceDTO(postId, username);

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // When / Then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            postService.deletePost(deleteDTO);
        });
        assertEquals(PostExceptionType.NOT_FOUND_POST, exception.getExceptionType());
    }

    @Test
    @DisplayName("게시글 삭제 시 작성자와 삭제 요청 사용자가 일치하지 않으면 예외가 발생해야 한다")
    public void testDeletePost_UnauthorizedUser() {
        // Given
        Long postId = 1L;
        String username = "otherUser";
        PostDeleteRequestServiceDTO deleteDTO = new PostDeleteRequestServiceDTO(postId, username);

        User postOwner = User.builder().username("ownerUser").build();
        ReflectionTestUtils.setField(postOwner, "userId", 1L);
        User currentUser = User.builder().username(username).build();
        ReflectionTestUtils.setField(currentUser, "userId", 2L);

        Post post = Post.builder().title("Test Title").content("Test Content").build();
        post.addUser(postOwner);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername(username)).thenReturn(currentUser);

        // When / Then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            postService.deletePost(deleteDTO);
        });
        assertEquals(PostExceptionType.UNAUTHORIZED_USER, exception.getExceptionType());
    }

    @Test
    @DisplayName("인기 게시글 조회 시 상위 10개 게시글을 반환해야 한다")
    public void testGetPopularPosts() {
        // Given
        User dummyUser = User.builder().username("dummyUser").build();

        ReflectionTestUtils.setField(dummyUser, "userId", 1L);

        Post post1 = Post.builder().title("Title1").content("Content1").build();
        Post post2 = Post.builder().title("Title2").content("Content2").build();
        post1.addUser(dummyUser);
        post2.addUser(dummyUser);

        List<Post> popularPosts = Arrays.asList(post1, post2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> popularPostsPage = new PageImpl<>(popularPosts, pageable, popularPosts.size());
        when(postRepository.findTop10ByOrderByViewsDescLikesDesc(pageable)).thenReturn(popularPostsPage);

        // When
        List<PostResponseDTO> responseList = postService.getPopularPosts();

        // Then
        assertNotNull(responseList);
        assertEquals(2, responseList.size());
        assertEquals("Title1", responseList.get(0).getTitle());
        assertEquals("Title2", responseList.get(1).getTitle());
    }

    @Test
    @DisplayName("좋아요 증가 메서드 테스트 (void 메서드)")
    public void testIncrementLikes() {
        // Given
        Long postId = 1L;
        Post post = spy(Post.builder().title("Test Title").content("Test Content").build());
        ReflectionTestUtils.setField(post, "likes", 3);
        User user = User.builder().username("testUser").build();
        ReflectionTestUtils.setField(user, "userId", 1L);
        post.addUser(user);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        // When
        postService.incrementLikes(postId);

        // Then
        verify(post, times(1)).incrementLikes();
        assertEquals(4, post.getLikes(), "좋아요 수는 1 증가해야 합니다.");
    }

    @Test
    @DisplayName("getPostById: 존재하는 게시글 조회 시 DTO 반환 및 조회수 1 증가")
    public void testGetPostById_Success() {
        Long postId = 1L;
        Post post = spy(Post.builder().title("T").content("C").build());

        User user = User.builder().username("u").build();
        ReflectionTestUtils.setField(user, "userId", 1L);
        post.addUser(user);

        Team team = Team.builder().name(TeamName.KIA).build();
        post.addTeam(team);

        when(postRepository.findByIdWithUserAndTeam(postId))
                .thenReturn(Optional.of(post));

        int beforeViews = post.getViews();
        PostResponseDTO dto = postService.getPostById(postId);

        assertNotNull(dto);
        assertEquals(beforeViews + 1, dto.getViews());
        verify(post).incrementViews();
    }


    @Test
    @DisplayName("getPostById: 없는 게시글 조회 시 BadRequestException")
    public void testGetPostById_NotFound() {
        Long postId = 1L;
        when(postRepository.findByIdWithUserAndTeam(postId))
                .thenReturn(Optional.empty());

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> postService.getPostById(postId)
        );
        assertEquals(PostExceptionType.NOT_FOUND_POST, ex.getExceptionType());
    }

    @Test
    @DisplayName("searchPosts: 키워드+타입으로 페이지 조회 후 DTO 매핑")
    public void testSearchPosts() {
        String keyword = "k";
        String type    = "title";
        Pageable pg    = PageRequest.of(0, 5);

        Post post = Post.builder().title("T").content("C").build();
        post.addUser(User.builder().username("u").build());
        Page<Post> page = new PageImpl<>(List.of(post), pg, 1);

        when(postRepository.searchPosts(keyword, type, pg))
                .thenReturn(page);

        Page<PostResponseDTO> result = postService.searchPosts(keyword, type, pg);
        assertEquals(1, result.getTotalElements());
        assertEquals("T", result.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("searchPostsByTeam: 유효한 팀명으로 페이지 조회")
    public void testSearchPostsByTeam_Success() {
        String teamName = "KIA";
        Pageable pg     = PageRequest.of(0, 5);

        Post post = Post.builder().title("T").content("C").build();
        post.addUser(User.builder().username("u").build());
        Page<Post> page = new PageImpl<>(List.of(post), pg, 1);

        when(postRepository.findByTeam_Name(TeamName.KIA, pg))
                .thenReturn(page);

        Page<PostResponseDTO> result = postService.searchPostsByTeam(teamName, pg);
        assertEquals(1, result.getTotalElements());
        assertEquals("T", result.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("searchPostsByTeam: 잘못된 팀명 입력 시 BadRequestException")
    public void testSearchPostsByTeam_InvalidTeam() {
        String badName = "NOT_A_TEAM";
        Pageable pg    = PageRequest.of(0, 5);

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> postService.searchPostsByTeam(badName, pg)
        );
        assertEquals(TeamExceptionType.INVALID_TEAM_NAME, ex.getExceptionType());
    }

    @Test
    @DisplayName("getPosts(page): 페이징 조회 후 DTO 매핑")
    public void testGetPostsWithPage() {
        int pageNo      = 2;
        Pageable pg     = PageRequest.of(pageNo, 20, Sort.by(Sort.Direction.DESC, "createdAt"));

        Post post = Post.builder().title("X").content("Y").build();
        post.addUser(User.builder().username("u").build());
        Page<Post> page = new PageImpl<>(List.of(post), pg, 1);

        when(postRepository.findAll(pg))
                .thenReturn(page);

        Page<PostResponseDTO> result = postService.getPosts(pageNo);

        assertEquals(1, result.getNumberOfElements());
        assertEquals("X", result.getContent().get(0).getTitle());
    }

    @Test
    @DisplayName("updatePost: 빈 타이틀 입력 시 INVALID_TITLE 예외")
    public void testUpdatePost_InvalidTitle() {
        Long postId = 1L;
        Post post = spy(Post.builder().title("Old").content("Old").build());
        User user = spy(User.builder().username("u").build());
        doReturn(1L).when(user).getUserId();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername("u")).thenReturn(user);
        doReturn(user).when(post).getUser();

        PostUpdateRequestServiceDTO dto =
                new PostUpdateRequestServiceDTO(postId, "u", "", "NewContent");

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> postService.updatePost(dto)
        );
        assertEquals(PostExceptionType.INVALID_TITLE, ex.getExceptionType());
    }

    @Test
    @DisplayName("updatePost: 빈 콘텐츠 입력 시 INVALID_CONTENT 예외")
    public void testUpdatePost_InvalidContent() {
        Long postId = 1L;
        Post post = spy(Post.builder().title("Old").content("Old").build());
        User user = spy(User.builder().username("u").build());
        doReturn(1L).when(user).getUserId();

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userService.getUserBySecurityUsername("u")).thenReturn(user);
        doReturn(user).when(post).getUser();

        PostUpdateRequestServiceDTO dto =
                new PostUpdateRequestServiceDTO(postId, "u", "NewTitle", "   ");

        BadRequestException ex = assertThrows(
                BadRequestException.class,
                () -> postService.updatePost(dto)
        );
        assertEquals(PostExceptionType.INVALID_CONTENT, ex.getExceptionType());
    }

    @Test
    @DisplayName("getPosts: 전체 게시글 조회 후 DTO 리스트 반환")
    public void testGetPosts_List() {
        // Given
        Post post1 = Post.builder().title("First").content("Content1").build();
        Post post2 = Post.builder().title("Second").content("Content2").build();

        User user = User.builder().username("user").build();
        post1.addUser(user);
        post2.addUser(user);
        Team team = Team.builder().name(TeamName.KIA).build();
        post1.addTeam(team);
        post2.addTeam(team);

        when(postRepository.findAll()).thenReturn(List.of(post1, post2));

        // When
        List<PostResponseDTO> result = postService.getPosts();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("First", result.get(0).getTitle());
        assertEquals("Second", result.get(1).getTitle());
        verify(postRepository, times(1)).findAll();
    }

}
