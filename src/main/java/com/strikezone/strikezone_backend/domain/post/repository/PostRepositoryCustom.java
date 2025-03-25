package com.strikezone.strikezone_backend.domain.post.repository;

import com.strikezone.strikezone_backend.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> searchPosts(String keyword, String searchType, Pageable pageable);

}
