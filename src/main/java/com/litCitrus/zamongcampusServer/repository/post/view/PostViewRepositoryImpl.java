package com.litCitrus.zamongcampusServer.repository.post.view;

import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.dto.post.PostSearch;
import com.litCitrus.zamongcampusServer.dto.post.QPostDtoRes_Res;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.litCitrus.zamongcampusServer.domain.post.QPost.post;
import static com.litCitrus.zamongcampusServer.domain.post.QPostLike.postLike;

@Repository
@RequiredArgsConstructor
public class PostViewRepositoryImpl implements PostViewRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<PostDtoRes.Res> searchPosts(PostSearch postSearch) {
        List<PostDtoRes.Res> posts = jpaQueryFactory.select(new QPostDtoRes_Res(post, postLike.isNotNull()))
                .from(post)
                .join(post.user).fetchJoin()
                .leftJoin(postLike)
                .on(postLike.post.eq(post).and(postLike.user.eq(postSearch.getLoggedUser())))
                .where(
                        isReadable(),
                        postSearch.getCollegeName() != null ?
                                post.user.college.collegeName.eq(postSearch.getCollegeName()) : null,
                        postSearch.getWriter() != null ?
                                post.user.eq(postSearch.getWriter()) : null,
                        postSearch.getOldestPost() != null ?
                                post.id.lt(postSearch.getOldestPost()) : null
                )
                .orderBy(post.createdAt.desc())
                .limit(postSearch.getPageSize())
                .fetch();
        return posts;
    }

    private BooleanExpression isReadable() {
        return post.deleted.isFalse().and(post.exposed.isTrue());
    }

}
