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
import com.strikezone.strikezone_backend.global.config.replica.ReadOnlyConnection;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import com.strikezone.strikezone_backend.global.exception.type.NotFoundException;
import com.strikezone.strikezone_backend.global.exception.type.UnAuthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto createComment(CommentCreateServiceDto dto) {
        Post post = postRepository.findById(dto.getPostId())
                                  .orElseThrow(() -> new NotFoundException(PostExceptionType.NOT_FOUND_POST));
        User user = userRepository.findById(dto.getUserId())
                                  .orElseThrow(() -> new NotFoundException(UserExceptionType.NOT_FOUND_USER));

        Comment comment = Comment.builder()
                                 .content(dto.getContent())
                                 .user(user)
                                 .build();

        comment.addPost(post);

        Comment savedComment = commentRepository.save(comment);

        return CommentResponseDto.builder()
                                 .commentId(savedComment.getCommentId())
                                 .postId(savedComment.getPost().getPostId())
                                 .userId(savedComment.getUser().getUserId())
                                 .content(savedComment.getContent())
                                 .build();
    }

    @Transactional
    public CommentResponseDto updateComment(CommentUpdateServiceDto dto) {
        Comment comment = commentRepository.findById(dto.getCommentId())
                                           .orElseThrow(() -> new NotFoundException(CommentExceptionType.NOT_FOUND_COMMENT));

        if (!comment.getUser().getUserId().equals(dto.getUserId())) {
            throw new BadRequestException(CommentExceptionType.UNAUTHORIZED_USER);
        }

        comment.updateContent(dto.getContent());

        return CommentResponseDto.builder()
                                 .commentId(comment.getCommentId())
                                 .postId(comment.getPost().getPostId())
                                 .userId(comment.getUser().getUserId())
                                 .content(comment.getContent())
                                 .build();
    }

    @Transactional
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                                           .orElseThrow(() -> new NotFoundException(CommentExceptionType.NOT_FOUND_COMMENT));

        if (!comment.getUser().getUserId().equals(userId)) {
            throw new UnAuthorizedException(CommentExceptionType.UNAUTHORIZED_USER);
        }

        commentRepository.delete(comment);
    }

    @ReadOnlyConnection
    public List<CommentResponseDto> getCommentsByPost(Long postId) {
        List<Comment> comments = commentRepository.findCommentsByPostId(postId);
        return comments.stream()
                       .map(comment -> CommentResponseDto.builder()
                                                         .commentId(comment.getCommentId())
                                                         .postId(comment.getPost().getPostId())
                                                         .userId(comment.getUser().getUserId())
                                                         .content(comment.getContent())
                                                         .build())
                       .collect(Collectors.toList());
    }

    @ReadOnlyConnection
    public Page<CommentResponseDto> getCommentsByPostWithPaging(Long postId, int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> comments = commentRepository.findCommentsByPostIdWithPaging(postId, pageable);

        return comments.map(comment -> CommentResponseDto.builder()
                                                         .commentId(comment.getCommentId())
                                                         .postId(comment.getPost().getPostId())
                                                         .userId(comment.getUser().getUserId())
                                                         .content(comment.getContent())
                                                         .build());
    }

}
