package com.strikezone.strikezone_backend.domain.post.service;

import com.strikezone.strikezone_backend.domain.post.dto.response.PostResponseDTO;
import com.strikezone.strikezone_backend.domain.post.dto.service.PostRequestServiceDTO;
import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.post.exception.PostExceptionType;
import com.strikezone.strikezone_backend.domain.post.repository.PostRepository;
import com.strikezone.strikezone_backend.domain.team.service.TeamService;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.service.UserService;
import com.strikezone.strikezone_backend.global.exception.type.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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


}
