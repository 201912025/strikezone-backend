package com.strikezone.strikezone_backend.domain.user.repository;

import com.strikezone.strikezone_backend.domain.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional // 테스트 후 데이터베이스 롤백
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("사용자의 아이디로 사용자 정보를 조회한다.")
    @Test
    void findByUsername() {
        // given
        User user1 = User.builder()
                .username("johndoe")
                .email("johndoe@example.com")
                .password("securepassword")
                .role("USER")
                .bio("Test User")
                .build();
        userRepository.save(user1);

        // when
        User foundUser = userRepository.findByUsername("johndoe");

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo("johndoe");
        assertThat(foundUser.getEmail()).isEqualTo("johndoe@example.com");
        assertThat(foundUser.getRole()).isEqualTo("USER");
    }

    @DisplayName("사용자 이름이 이미 존재하는지 확인한다.")
    @Test
    void existsByUsername() {
        // given
        User user1 = User.builder()
                .username("johndoe")
                .email("johndoe@example.com")
                .password("securepassword")
                .role("USER")
                .bio("Test User")
                .build();
        userRepository.save(user1);

        // when
        boolean exists = userRepository.existsByUsername("johndoe");

        // then
        assertThat(exists).isTrue();
    }
}
