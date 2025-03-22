package com.strikezone.strikezone_backend.domain.post.repository;

import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Boolean existsByTitle(String title);

    @EntityGraph(attributePaths = {"user", "team"})
    List<Post> findTop10ByOrderByViewsDescLikesDesc();

    @EntityGraph(attributePaths = {"user", "team"})
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "team"})
    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // 내용만 검색
    @EntityGraph(attributePaths = {"user", "team"})
    Page<Post> findByContentContainingIgnoreCase(String content, Pageable pageable);

    // 작성자(유저 이름)로 검색
    @EntityGraph(attributePaths = {"user", "team"})
    Page<Post> findByUserUsernameContainingIgnoreCase(String username, Pageable pageable);

    // 팀 이름으로 검색
    @EntityGraph(attributePaths = {"user", "team"})
    Page<Post> findByTeam_Name(TeamName teamName, Pageable pageable);

    // 단건 조회 시 연관 엔티티를 페치 조인하여 로딩
    @Query("select p from Post p " +
            "left join fetch p.user " +
            "left join fetch p.team " +
            "where p.postId = :postId")
    Optional<Post> findByIdWithUserAndTeam(@Param("postId") Long postId);
}
