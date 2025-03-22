package com.strikezone.strikezone_backend.domain.post.repository;

import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Boolean existsByTitle(String title);

    List<Post> findTop10ByOrderByViewsDescLikesDesc();


    // 제목과 내용을 동시에 검색 (대소문자 구분없이)
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    // 제목만 검색
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // 내용만 검색
    Page<Post> findByContentContainingIgnoreCase(String content, Pageable pageable);

    // 작성자(유저 이름)로 검색 - Post 엔티티가 User와 연관관계가 있고, User에 username 필드가 있다고 가정
    Page<Post> findByUserUsernameContainingIgnoreCase(String username, Pageable pageable);

    Page<Post> findByTeam_Name(TeamName teamName, Pageable pageable);

    @Query("select p from Post p" +
            "left join fetch p.user" +
            "left join fetch p.team" +
            "where p.postId = :postId")
    Optional<Post> findByIdWithUserAndTeam(@Param("postId") Long postId);

}
