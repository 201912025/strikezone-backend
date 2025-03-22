package com.strikezone.strikezone_backend.domain.comment.repository;

import com.strikezone.strikezone_backend.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("select c from Comment c where c.post.postId = :postId")
    List<Comment> findCommentsByPostId(@Param("postId") Long postId);

    @Query("select c from Comment c where c.post.postId = :postId")
    Page<Comment> findCommentsByPostIdWithPaging(@Param("postId") Long postId, Pageable pageable);

}
