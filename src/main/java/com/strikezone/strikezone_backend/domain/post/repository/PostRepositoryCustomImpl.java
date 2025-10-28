package com.strikezone.strikezone_backend.domain.post.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.strikezone.strikezone_backend.domain.post.entity.Post;
import com.strikezone.strikezone_backend.domain.post.entity.QPost;
import com.strikezone.strikezone_backend.domain.user.entity.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Post> searchPosts(String keyword, String searchType, Pageable pageable) {
        QPost post = QPost.post;
        QUser user = QUser.user;

        BooleanBuilder builder = new BooleanBuilder();
        if ("title".equalsIgnoreCase(searchType)) {
            builder.and(post.title.containsIgnoreCase(keyword));
        } else if ("content".equalsIgnoreCase(searchType)) {
            builder.and(post.content.containsIgnoreCase(keyword));
        } else if ("author".equalsIgnoreCase(searchType)) {
            builder.and(post.user.username.containsIgnoreCase(keyword));
        } else {
            builder.and(post.title.containsIgnoreCase(keyword)
                                  .or(post.content.containsIgnoreCase(keyword)));
        }

        OrderSpecifier<?> orderBy = post.createdAt.desc();

        JPAQuery<Post> query = queryFactory.selectFrom(post)
                                           .leftJoin(post.user, user).fetchJoin()
                                           .where(builder)
                                           .orderBy(orderBy);

        List<Post> results = query.offset(pageable.getOffset())
                                  .limit(pageable.getPageSize())
                                  .fetch();

        long total = queryFactory.selectFrom(post)
                                 .where(builder)
                                 .fetchCount();

        return new PageImpl<>(results, pageable, total);
    }
}
