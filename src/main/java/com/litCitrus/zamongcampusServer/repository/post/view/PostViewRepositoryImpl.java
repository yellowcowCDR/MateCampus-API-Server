package com.litCitrus.zamongcampusServer.repository.post.view;

import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.dto.post.PostSearch;
import com.litCitrus.zamongcampusServer.dto.post.QPostDtoRes_Res;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.litCitrus.zamongcampusServer.domain.post.QPost.post;
import static com.litCitrus.zamongcampusServer.domain.post.QPostLike.postLike;

@Repository
@RequiredArgsConstructor
public class PostViewRepositoryImpl implements PostViewRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostDtoRes.Res> searchPosts(PostSearch postSearch, Pageable pageable) {
        List<PostDtoRes.Res> posts = jpaQueryFactory.select(new QPostDtoRes_Res(post, postLike.isNotNull()))
                .from(post)
                .join(post.user).fetchJoin()
                .leftJoin(postLike)
                .on(postLike.post.eq(post).and(postLike.user.eq(postSearch.getLoggedUser())))
                .where(
                        isReadable(),
                        postSearch.getCollegeCode() != null ?
                                post.user.collegeCode.eq(postSearch.getCollegeCode()) : null
                )
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return posts;
    }

    private BooleanExpression isReadable() {
        return post.deleted.isFalse().and(post.exposed.isTrue());
    }

}
