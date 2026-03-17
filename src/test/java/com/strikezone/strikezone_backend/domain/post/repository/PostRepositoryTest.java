package com.strikezone.strikezone_backend.domain.post.repository;

import com.strikezone.strikezone_backend.domain.config.QuerydslTestConfig;
import com.strikezone.strikezone_backend.domain.config.TestRedisCacheConfig;
import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.team.entity.Team;
import com.strikezone.strikezone_backend.domain.team.entity.TeamName;
import com.strikezone.strikezone_backend.domain.team.repository.TeamRepository;
import com.strikezone.strikezone_backend.domain.user.entity.User;
import com.strikezone.strikezone_backend.domain.user.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({QuerydslTestConfig.class, TestRedisCacheConfig.class}) // 필요한 테스트 전용 설정들을 Import
public class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private EntityManager em;

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

    @Test
    @DisplayName("N+1 방어: findAllPostsWithDetails 실행 시 User와 Team이 함께 페치 조인되어야 한다")
    public void testFindAllPostsWithDetails_FetchJoin() {
        // Given: 1. 유저 생성
        User user = User.builder()
                        .username("testUser")
                        .email("test@example.com")
                        .password("password")
                        .role("USER")
                        .build();
        userRepository.save(user);

        // Given: 2. 팀 생성
        Team team = Team.builder()
                        .name(TeamName.KIA)
                        .build();
        teamRepository.save(team);

        // Given: 3. 팀이 '있는' 게시글 저장
        Post postWithTeam = Post.builder().title("팀 있는 글").content("내용1").build();
        postWithTeam.addUser(user);
        postWithTeam.addTeam(team);
        postRepository.save(postWithTeam);

        // Given: 4. 팀이 '없는' 게시글 저장 (Left Join이 잘 작동하는지 확인하기 위함)
        Post postWithoutTeam = Post.builder().title("팀 없는 글").content("내용2").build();
        postWithoutTeam.addUser(user);
        postRepository.save(postWithoutTeam);

        // [핵심] 영속성 컨텍스트(1차 캐시) 강제 비우기
        // 이걸 안 하면 DB에 쿼리를 날리지 않고 캐시에서 가져오므로 페치 조인 테스트가 무의미해집니다.
        em.flush();
        em.clear();

        // When: 페이징 조회 실행
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Post> result = postRepository.findAllPostsWithDetails(pageRequest);

        // Then: 결과 검증
        List<Post> posts = result.getContent();
        assertEquals(2, posts.size(), "총 2개의 게시글이 조회되어야 한다");

        // 1. 팀이 있는 게시글 검증
        Post firstPost = posts.stream().filter(p -> p.getTitle().equals("팀 있는 글")).findFirst().get();
        assertEquals("testUser", firstPost.getUser().getUsername(), "유저 정보가 로드되어야 한다");
        assertNotNull(firstPost.getTeam(), "팀 정보가 존재해야 한다");
        assertEquals(TeamName.KIA, firstPost.getTeam().getName());

        // 2. 팀이 없는 게시글 검증 (일반 Join이었다면 이 게시글은 누락되었을 것임)
        Post secondPost = posts.stream().filter(p -> p.getTitle().equals("팀 없는 글")).findFirst().get();
        assertEquals("testUser", secondPost.getUser().getUsername(), "유저 정보가 로드되어야 한다");
        assertNull(secondPost.getTeam(), "팀 정보는 null이어야 한다");
    }

}
