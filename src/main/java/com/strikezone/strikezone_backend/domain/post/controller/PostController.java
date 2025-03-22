package com.strikezone.strikezone_backend.domain.post.controller;

import com.strikezone.strikezone_backend.domain.post.dto.controller.PostRequestDTO;
import com.strikezone.strikezone_backend.domain.post.dto.controller.PostUpdateRequestDTO;
import com.strikezone.strikezone_backend.domain.post.dto.response.PostResponseDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostDeleteRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostUpdateRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping("/paged")
    public ResponseEntity<Page<PostResponseDTO>> getPostsPaged(Pageable pageable) {
        Page<PostResponseDTO> postsPage = postService.getPosts(pageable);
        return ResponseEntity.ok(postsPage);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<PostResponseDTO>> searchPosts(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "searchType", required = false, defaultValue = "all") String searchType,
            Pageable pageable) {
        Page<PostResponseDTO> postsPage = postService.searchPosts(keyword, searchType, pageable);
        return ResponseEntity.ok(postsPage);
    }

    @GetMapping("/team")
    public ResponseEntity<Page<PostResponseDTO>> searchPostsByTeam(
            @RequestParam("teamName") String teamName,
            Pageable pageable) {
        Page<PostResponseDTO> postsPage = postService.searchPostsByTeam(teamName, pageable);
        return ResponseEntity.ok(postsPage);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {
        PostResponseDTO post = postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long postId,
                                                      @RequestBody PostUpdateRequestDTO updateRequest) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PostUpdateRequestServiceDTO serviceDTO = PostUpdateRequestServiceDTO.from(updateRequest, postId, username);
        PostResponseDTO postResponseDTO = postService.updatePost(serviceDTO);
        return ResponseEntity.ok(postResponseDTO);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        PostDeleteRequestServiceDTO deleteRequestDTO = PostDeleteRequestServiceDTO.builder()
                                                                                  .postId(postId)
                                                                                  .username(username)
                                                                                  .build();
        postService.deletePost(deleteRequestDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<PostResponseDTO>> getPopularPosts() {
        List<PostResponseDTO> popularPosts = postService.getPopularPosts();
        return ResponseEntity.ok(popularPosts);
    }

    @PatchMapping("/{postId}/like")
    public ResponseEntity<Void> incrementLikes(@PathVariable Long postId) {
        postService.incrementLikes(postId);
        return ResponseEntity.noContent().build();
    }
}
