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

import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Boolean existsByTitle(String title);

    // Top 10 게시글 조회 (정렬만 처리, 연관 엔티티는 엔티티 그래프로 로딩)
    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p order by p.views desc, p.likes desc")
    Page<Post> findTop10ByOrderByViewsDescLikesDesc(Pageable pageable);

    // 제목과 내용을 동시에 검색 (EntityGraph 적용)
    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p where lower(p.title) like lower(concat('%', :keyword, '%')) " +
            "or lower(p.content) like lower(concat('%', :keyword, '%'))")
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(@Param("keyword") String keyword, Pageable pageable);

    // 제목만 검색 (EntityGraph 적용)
    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p where lower(p.title) like lower(concat('%', :title, '%'))")
    Page<Post> findByTitleContainingIgnoreCase(@Param("title") String title, Pageable pageable);

    // 내용만 검색 (EntityGraph 적용)
    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p where lower(p.content) like lower(concat('%', :content, '%'))")
    Page<Post> findByContentContainingIgnoreCase(@Param("content") String content, Pageable pageable);

    // 작성자(유저 이름)로 검색 (EntityGraph 적용)
    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p join p.user u where lower(u.username) like lower(concat('%', :username, '%'))")
    Page<Post> findByUserUsernameContainingIgnoreCase(@Param("username") String username, Pageable pageable);

    // 팀 이름(TeamName Enum)으로 검색 (EntityGraph 적용)
    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p join p.team t where t.name = :teamName")
    Page<Post> findByTeam_Name(@Param("teamName") TeamName teamName, Pageable pageable);

    // 단건 조회 시 연관 엔티티 로딩 (EntityGraph 적용)
    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p " +
            "join fetch p.user " +
            "left join fetch p.team " +
            "where p.postId = :postId")
    Optional<Post> findByIdWithUserAndTeam(@Param("postId") Long postId);

}
