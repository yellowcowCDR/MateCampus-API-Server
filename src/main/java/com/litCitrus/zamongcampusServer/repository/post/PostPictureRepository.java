package com.litCitrus.zamongcampusServer.repository.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostPictureRepository extends JpaRepository<PostPicture, Long> {

    List<PostPicture> findAllByPost(Post post);
}

