package com.strikezone.strikezone_backend.domain.post.service;

import com.strikezone.strikezone_backend.domain.post.dto.response.PostResponseDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostDeleteRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostUpdateRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.post.exception.PostExceptionType;
import com.strikezone.strikezone_backend.domain.post.repository.PostRepository;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.exception.TeamExceptionType;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import com.strikezone.strikezone_backend.global.config.replica.ReadOnlyConnection;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final TeamService teamService;
    private final UserService userService;

    @Transactional
    public PostResponseDTO createPost(PostRequestServiceDTO postRequestServiceDTO) {

        if (postRepository.existsByTitle(postRequestServiceDTO.getTitle())) {
            throw new BadRequestException(PostExceptionType.DUPLICATED_TITLE);
        }

        Post post = Post.builder()
                        .title(postRequestServiceDTO.getTitle())
                        .content(postRequestServiceDTO.getContent())
                        .build();

        User writedUser = userService.getUserBySecurityUsername(postRequestServiceDTO.getUsername());

        post.addTeam(teamService.findByTeamName(postRequestServiceDTO.getTeamName()));
        post.addUser(writedUser);

        Post savedPost = postRepository.save(post);

        return PostResponseDTO.fromEntity(savedPost);
    }

    @ReadOnlyConnection
    public List<PostResponseDTO> getPosts() {
        List<Post> posts = postRepository.findAll();
        return PostResponseDTO.fromEntities(posts);
    }

    @Transactional
    public PostResponseDTO getPostById(Long postId) {
        Post post = postRepository.findByIdWithUserAndTeam(postId)
                                  .orElseThrow(() -> new BadRequestException(PostExceptionType.NOT_FOUND_POST));
        post.incrementViews();
        return PostResponseDTO.fromEntity(post);
    }

    @Transactional
    public PostResponseDTO updatePost(PostUpdateRequestServiceDTO postUpdateRequestServiceDTO) {
        Post post = postRepository.findById(postUpdateRequestServiceDTO.getPostId())
                                  .orElseThrow(() -> new BadRequestException(PostExceptionType.NOT_FOUND_POST));

        User currentUser = userService.getUserBySecurityUsername(postUpdateRequestServiceDTO.getUsername());
        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new BadRequestException(PostExceptionType.UNAUTHORIZED_USER);
        }

        validateTitle(postUpdateRequestServiceDTO.getTitle());
        validateContent(postUpdateRequestServiceDTO.getContent());

        if (post.getTitle().equals(postUpdateRequestServiceDTO.getTitle()) &&
                post.getContent().equals(postUpdateRequestServiceDTO.getContent())) {
            return PostResponseDTO.fromEntity(post);
        }

        post.update(postUpdateRequestServiceDTO.getTitle(), postUpdateRequestServiceDTO.getContent());

        return PostResponseDTO.fromEntity(post);
    }

    private void validateTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new BadRequestException(PostExceptionType.INVALID_TITLE);
        }
        if (postRepository.existsByTitle(title)) {
            throw new BadRequestException(PostExceptionType.DUPLICATED_TITLE);
        }
    }

    private void validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new BadRequestException(PostExceptionType.INVALID_CONTENT);
        }
    }

    @Transactional
    public void deletePost(PostDeleteRequestServiceDTO postDeleteRequestServiceDTO) {
        Post post = postRepository.findById(postDeleteRequestServiceDTO.getPostId())
                                  .orElseThrow(() -> new BadRequestException(PostExceptionType.NOT_FOUND_POST));

        User currentUser = userService.getUserBySecurityUsername(postDeleteRequestServiceDTO.getUsername());
        if (!post.getUser().getUserId().equals(currentUser.getUserId())) {
            throw new BadRequestException(PostExceptionType.UNAUTHORIZED_USER);
        }
        postRepository.delete(post);
    }

    @Cacheable(cacheNames = "popularPosts", key = "'popularPosts'", cacheManager = "postCacheManager")
    @ReadOnlyConnection
    public List<PostResponseDTO> getPopularPosts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> popularPosts = postRepository.findTop10ByOrderByViewsDescLikesDesc(pageable);
        return popularPosts.map(PostResponseDTO::fromEntity).getContent();
    }

    @Transactional
    public void incrementLikes(Long postId) {
        Post post = postRepository.findById(postId)
                                  .orElseThrow(() -> new BadRequestException(PostExceptionType.NOT_FOUND_POST));
        post.incrementLikes();
    }

    @ReadOnlyConnection
    public Page<PostResponseDTO> searchPosts(String keyword, String searchType, Pageable pageable) {
        Page<Post> posts = postRepository.searchPosts(keyword, searchType, pageable);
        return posts.map(PostResponseDTO::fromEntity);
    }

    @ReadOnlyConnection
    public Page<PostResponseDTO> searchPostsByTeam(String teamName, Pageable pageable) {
        TeamName teamNameEnum;
        try {
            teamNameEnum = TeamName.valueOf(teamName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(TeamExceptionType.INVALID_TEAM_NAME);
        }
        Page<Post> posts = postRepository.findByTeam_Name(teamNameEnum, pageable);
        return posts.map(PostResponseDTO::fromEntity);

    }

    @ReadOnlyConnection
    // 페이징 및 정렬이 적용된 전체 게시글 조회 (Page 버전)
    public Page<PostResponseDTO> getPosts(int page) {
        Pageable pageable = PageRequest.of(page, 20, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> posts = postRepository.findAll(pageable);
        return posts.map(PostResponseDTO::fromEntity);
    }

}
