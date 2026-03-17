package com.strikezone.strikezone_backend.domain.post.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.strikezone.strikezone_backend.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.strikezone.strikezone_backend.domain.post.entity.QPost.post;
import static com.strikezone.strikezone_backend.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> searchPosts(String keyword, String searchType, Pageable pageable) {

        List<Post> results = queryFactory
                .selectFrom(post)
                .leftJoin(post.user, user).fetchJoin()
                .where(createSearchCondition(searchType, keyword))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .selectFrom(post)
                .where(createSearchCondition(searchType, keyword))
                .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }

    private BooleanExpression createSearchCondition(String searchType, String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        if ("title".equalsIgnoreCase(searchType)) {
            return titleContains(keyword);
        }
        if ("content".equalsIgnoreCase(searchType)) {
            return contentContains(keyword);
        }
        if ("author".equalsIgnoreCase(searchType)) {
            return authorContains(keyword);
        }
        // 기본값: 제목 OR 내용
        return titleContains(keyword).or(contentContains(keyword));
    }

    private BooleanExpression titleContains(String keyword) {
        return post.title.containsIgnoreCase(keyword);
    }

    private BooleanExpression contentContains(String keyword) {
        return post.content.containsIgnoreCase(keyword);
    }

    private BooleanExpression authorContains(String keyword) {
        return post.user.username.containsIgnoreCase(keyword);
    }
}