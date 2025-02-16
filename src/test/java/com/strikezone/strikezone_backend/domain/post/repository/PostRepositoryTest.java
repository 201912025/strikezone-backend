package com.strikezone.strikezone_backend.domain.post.repository;

import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

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
}
