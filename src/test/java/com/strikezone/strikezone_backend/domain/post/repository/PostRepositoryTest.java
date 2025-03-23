package com.strikezone.strikezone_backend.domain.post.repository;

import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("게시글 제목이 존재하면 true를 반환해야 한다")
    public void testExistsByTitle_True() {
        // Given
        User user = User.builder()
                        .username("testUser")
                        .email("test@example.com")
                        .password("password")
                        .role("USER")
                        .build();

        user = userRepository.save(user);

        Post post = Post.builder()
                        .title("테스트 제목")
                        .content("테스트 내용")
                        .build();

        post.addUser(user);

        postRepository.save(post);

        // When
        Boolean exists = postRepository.existsByTitle("테스트 제목");

        // Then
        assertTrue(exists, "주어진 제목의 게시글이 존재해야 한다");
    }


    @Test
    @DisplayName("게시글 제목이 존재하지 않으면 false를 반환해야 한다")
    public void testExistsByTitle_False() {
        // When
        Boolean exists = postRepository.existsByTitle("존재하지 않는 제목");

        // Then
        assertFalse(exists, "주어진 제목의 게시글이 존재하지 않아야 한다");
    }

    @Test
    @DisplayName("전체 게시글을 조회할 수 있어야 한다")
    public void testFindAll() {
        // Given
        User user = User.builder()
                        .username("testUser")
                        .email("test@example.com")
                        .password("password")
                        .role("USER")
                        .build();

        user = userRepository.save(user);

        Post post1 = Post.builder()
                         .title("Post 1")
                         .content("Content 1")
                         .build();
        post1.addUser(user);

        Post post2 = Post.builder()
                         .title("Post 2")
                         .content("Content 2")
                         .build();
        post2.addUser(user);

        postRepository.save(post1);
        postRepository.save(post2);

        // When
        List<Post> posts = postRepository.findAll();

        // Then
        assertNotNull(posts, "게시글 리스트는 null이 아니어야 한다");
        assertEquals(2, posts.size(), "전체 게시글 수가 2여야 한다");
    }

    @Test
    @DisplayName("인기 게시글 조회 시 상위 10개 게시글을 반환해야 한다")
    public void testFindTop10ByOrderByViewsDescLikesDesc() {
        // Given
        User user = User.builder()
                        .username("dummyUser")
                        .email("dummy@example.com")
                        .password("dummyPassword")
                        .role("USER")
                        .build();
        user = userRepository.save(user);

        Post post1 = Post.builder()
                         .title("Title1")
                         .content("Content1")
                         .build();
        post1.addUser(user);

        Post post2 = Post.builder()
                         .title("Title2")
                         .content("Content2")
                         .build();
        post2.addUser(user);

        postRepository.save(post1);
        postRepository.save(post2);


        // When
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> popularPosts = postRepository.findTop10ByOrderByViewsDescLikesDesc(pageable);

        // Then
        assertNotNull(popularPosts, "조회 결과는 null이 아니어야 한다");
        assertTrue(popularPosts.getContent().size() >= 2, "최소 2개의 게시글이 조회되어야 한다");
        assertEquals("Title1", popularPosts.getContent().get(0).getTitle());
        assertEquals("Title2", popularPosts.getContent().get(1).getTitle());
    }

}
