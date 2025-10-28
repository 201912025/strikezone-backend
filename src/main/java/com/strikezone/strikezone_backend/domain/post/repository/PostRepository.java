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
public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    Boolean existsByTitle(String title);

    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p order by p.likes desc, p.views desc")
    Page<Post> findTop10ByOrderByViewsDescLikesDesc(Pageable pageable);

    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p join p.user u where lower(u.username) like lower(concat('%', :username, '%'))")
    Page<Post> findByUserUsernameContainingIgnoreCase(@Param("username") String username, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p join p.team t where t.name = :teamName")
    Page<Post> findByTeam_Name(@Param("teamName") TeamName teamName, Pageable pageable);

    @EntityGraph(attributePaths = {"user", "team"})
    @Query("select p from Post p " +
            "join fetch p.user " +
            "left join fetch p.team " +
            "where p.postId = :postId")
    Optional<Post> findByIdWithUserAndTeam(@Param("postId") Long postId);

}
