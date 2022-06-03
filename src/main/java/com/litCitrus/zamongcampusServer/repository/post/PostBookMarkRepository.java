package com.litCitrus.zamongcampusServer.repository.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostBookMark;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostBookMarkRepository extends JpaRepository<PostBookMark, Long> {

    PostBookMark findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
}
