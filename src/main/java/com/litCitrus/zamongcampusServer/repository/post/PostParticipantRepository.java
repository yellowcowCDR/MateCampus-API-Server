package com.litCitrus.zamongcampusServer.repository.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostParticipant;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostParticipantRepository extends JpaRepository<PostParticipant, Long> {

    List<PostParticipant> findAllByPost(Post post);
    boolean existsByUserAndPost(User user, Post post);
    int countAllByPost(Post post);

}
