package com.strikezone.strikezone_backend.domain.comment.service;

import com.strikezone.strikezone_backend.domain.comment.dto.request.service.CommentCreateServiceDto;
import com.strikezone.strikezone_backend.domain.comment.dto.request.service.CommentUpdateServiceDto;
import com.strikezone.strikezone_backend.domain.comment.dto.response.CommentResponseDto;
import com.strikezone.strikezone_backend.domain.comment.entity.Comment;
import com.strikezone.strikezone_backend.domain.comment.exception.CommentExceptionType;
import com.strikezone.strikezone_backend.domain.comment.repository.CommentRepository;
import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.post.exception.PostExceptionType;
import com.strikezone.strikezone_backend.domain.post.repository.PostRepository;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.exception.UserExceptionType;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import com.strikezone.strikezone_backend.global.exception.type.UnAuthorizedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 생성이 정상적으로 수행된다")
    void createComment_success() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        String content = "Test comment";
        CommentCreateServiceDto createDto = CommentCreateServiceDto.builder()
                                                                   .postId(postId)
                                                                   .userId(userId)
                                                                   .content(content)
                                                                   .build();

        // Post와 User는 기본키가 자동 생성되므로 ReflectionTestUtils로 id 설정
        Post post = Post.builder().build();
        ReflectionTestUtils.setField(post, "postId", postId);
        User user = User.builder().build();
        ReflectionTestUtils.setField(user, "userId", userId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Comment comment = Comment.builder()
                                 .content(content)
                                 .post(post)
                                 .user(user)
                                 .build();
        // comment의 기본키 자동생성을 모방
        ReflectionTestUtils.setField(comment, "commentId", 1L);
        when(commentRepository.save(any(Comment.class))).thenReturn(comment);

        // when
        CommentResponseDto responseDto = commentService.createComment(createDto);

        // then
        assertNotNull(responseDto);
        assertEquals(1L, responseDto.getCommentId());
        assertEquals(postId, responseDto.getPostId());
        assertEquals(userId, responseDto.getUserId());
        assertEquals(content, responseDto.getContent());
    }

    @Test
    @DisplayName("게시글이 존재하지 않아 댓글 생성에 실패한다")
    void createComment_postNotFound() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        CommentCreateServiceDto createDto = CommentCreateServiceDto.builder()
                                                                   .postId(postId)
                                                                   .userId(userId)
                                                                   .content("Test comment")
                                                                   .build();

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            commentService.createComment(createDto);
        });
        assertEquals(PostExceptionType.NOT_FOUND_POST.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("사용자가 존재하지 않아 댓글 생성에 실패한다")
    void createComment_userNotFound() {
        // given
        Long postId = 1L;
        Long userId = 1L;
        CommentCreateServiceDto createDto = CommentCreateServiceDto.builder()
                                                                   .postId(postId)
                                                                   .userId(userId)
                                                                   .content("Test comment")
                                                                   .build();

        Post post = Post.builder().build();
        ReflectionTestUtils.setField(post, "postId", postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when & then
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            commentService.createComment(createDto);
        });
        assertEquals(UserExceptionType.NOT_FOUND_USER.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("댓글 수정이 정상적으로 수행된다")
    void updateComment_success() {
        // given
        Long commentId = 1L;
        Long userId = 1L;
        String updatedContent = "Updated comment";
        CommentUpdateServiceDto updateDto = CommentUpdateServiceDto.builder()
                                                                   .commentId(commentId)
                                                                   .userId(userId)
                                                                   .content(updatedContent)
                                                                   .build();

        Post post = Post.builder().build();
        ReflectionTestUtils.setField(post, "postId", 1L);
        User user = User.builder().build();
        ReflectionTestUtils.setField(user, "userId", userId);
        Comment comment = Comment.builder()
                                 .content("Old comment")
                                 .post(post)
                                 .user(user)
                                 .build();
        ReflectionTestUtils.setField(comment, "commentId", commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when: update 메서드 호출
        CommentResponseDto responseDto = commentService.updateComment(updateDto);

        // then
        assertNotNull(responseDto);
        assertEquals(updatedContent, responseDto.getContent());
    }

    @Test
    @DisplayName("작성자와 다른 사용자가 댓글 수정을 시도하여 실패한다")
    void updateComment_unauthorized() {
        // given
        Long commentId = 1L;
        Long writerUserId = 1L;
        Long differentUserId = 2L;
        CommentUpdateServiceDto updateDto = CommentUpdateServiceDto.builder()
                                                                   .commentId(commentId)
                                                                   .userId(differentUserId)
                                                                   .content("Updated comment")
                                                                   .build();

        Post post = Post.builder().build();
        ReflectionTestUtils.setField(post, "postId", 1L);
        User user = User.builder().build();
        ReflectionTestUtils.setField(user, "userId", writerUserId);
        Comment comment = Comment.builder()
                                 .content("Old comment")
                                 .post(post)
                                 .user(user)
                                 .build();
        ReflectionTestUtils.setField(comment, "commentId", commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when & then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            commentService.updateComment(updateDto);
        });
        assertEquals(CommentExceptionType.UNAUTHORIZED_USER.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("댓글 삭제가 정상적으로 수행된다")
    void deleteComment_success() {
        // given
        Long commentId = 1L;
        Long userId = 1L;
        Post post = Post.builder().build();
        ReflectionTestUtils.setField(post, "postId", 1L);
        User user = User.builder().build();
        ReflectionTestUtils.setField(user, "userId", userId);
        Comment comment = Comment.builder()
                                 .content("Test comment")
                                 .post(post)
                                 .user(user)
                                 .build();
        ReflectionTestUtils.setField(comment, "commentId", commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when
        assertDoesNotThrow(() -> commentService.deleteComment(commentId, userId));

        // then
        verify(commentRepository, times(1)).delete(comment);
    }

    @Test
    @DisplayName("작성자와 다른 사용자가 댓글 삭제를 시도하여 실패한다")
    void deleteComment_unauthorized() {
        // given
        Long commentId = 1L;
        Long writerUserId = 1L;
        Long differentUserId = 2L;
        Post post = Post.builder().build();
        ReflectionTestUtils.setField(post, "postId", 1L);
        User user = User.builder().build();
        ReflectionTestUtils.setField(user, "userId", writerUserId);
        Comment comment = Comment.builder()
                                 .content("Test comment")
                                 .post(post)
                                 .user(user)
                                 .build();
        ReflectionTestUtils.setField(comment, "commentId", commentId);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when & then
        UnAuthorizedException exception = assertThrows(UnAuthorizedException.class, () -> {
            commentService.deleteComment(commentId, differentUserId);
        });
        assertEquals(CommentExceptionType.UNAUTHORIZED_USER.getMessage(), exception.getMessage());
    }

    @Test
    @DisplayName("게시글에 속한 댓글 목록을 정상적으로 조회한다")
    void getCommentsByPost_success() {
        // given
        Long postId = 1L;
        Post post = Post.builder().build();
        ReflectionTestUtils.setField(post, "postId", postId);
        User user = User.builder().build();
        ReflectionTestUtils.setField(user, "userId", 1L);
        Comment comment1 = Comment.builder()
                                  .content("Comment 1")
                                  .post(post)
                                  .user(user)
                                  .build();
        ReflectionTestUtils.setField(comment1, "commentId", 1L);
        Comment comment2 = Comment.builder()
                                  .content("Comment 2")
                                  .post(post)
                                  .user(user)
                                  .build();
        ReflectionTestUtils.setField(comment2, "commentId", 2L);

        when(commentRepository.findByPostId(postId)).thenReturn(Arrays.asList(comment1, comment2));

        // when
        List<CommentResponseDto> commentList = commentService.getCommentsByPost(postId);

        // then
        assertNotNull(commentList);
        assertEquals(2, commentList.size());
        assertEquals("Comment 1", commentList.get(0).getContent());
        assertEquals("Comment 2", commentList.get(1).getContent());
    }
}
