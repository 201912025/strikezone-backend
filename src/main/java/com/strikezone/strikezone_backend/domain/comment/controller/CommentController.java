package com.strikezone.strikezone_backend.domain.comment.controller;

import com.strikezone.strikezone_backend.domain.comment.dto.request.controller.CommentCreateRequest;
import com.strikezone.strikezone_backend.domain.comment.dto.request.controller.CommentUpdateRequest;
import com.strikezone.strikezone_backend.domain.comment.dto.request.service.CommentCreateServiceDto;
import com.strikezone.strikezone_backend.domain.comment.dto.request.service.CommentUpdateServiceDto;
import com.strikezone.strikezone_backend.domain.comment.dto.response.CommentResponseDto;
import com.strikezone.strikezone_backend.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentCreateRequest request) {
        CommentCreateServiceDto serviceDto = CommentCreateServiceDto.from(request);
        CommentResponseDto response = commentService.createComment(serviceDto);

        URI location = URI.create("/api/comments" + response.getCommentId());

        return ResponseEntity.created(location).body(response);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long commentId,
                                                         @RequestBody CommentUpdateRequest request) {
        CommentUpdateServiceDto serviceDto = CommentUpdateServiceDto.from(request, commentId);

        CommentResponseDto response = commentService.updateComment(serviceDto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId,
                                              @RequestParam Long userId) {
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<CommentResponseDto>> getCommentsByPost(@RequestParam Long postId, @RequestParam int page) {
        Page<CommentResponseDto> responses = commentService.getCommentsByPostWithPaging(postId, page);
        return ResponseEntity.ok(responses);
    }
}
