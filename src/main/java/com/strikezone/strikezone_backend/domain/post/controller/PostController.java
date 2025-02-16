package com.strikezone.strikezone_backend.domain.post.controller;

import com.strikezone.strikezone_backend.domain.post.dto.controller.PostRequestDTO;
import com.strikezone.strikezone_backend.domain.post.dto.response.PostResponseDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@Valid @RequestBody PostRequestDTO postRequestDTO) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PostResponseDTO postResponseDTO = postService.createPost(PostRequestServiceDTO.from(postRequestDTO, username));

        URI location = URI.create("/api/posts/" + postResponseDTO.getPostId());

        return ResponseEntity.created(location).body(postResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getPosts() {
        List<PostResponseDTO> postResponseDTOS = postService.getPosts();

        return ResponseEntity.ok(postResponseDTOS);
    }

}
