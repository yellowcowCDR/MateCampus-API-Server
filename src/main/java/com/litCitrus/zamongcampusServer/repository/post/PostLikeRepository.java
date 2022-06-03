package com.litCitrus.zamongcampusServer.repository.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long> {

    PostLike findByUserAndPost(User user, Post post);
    boolean existsByUserAndPost(User user, Post post);
    List<PostLike> findAllByUser(User user);
}
