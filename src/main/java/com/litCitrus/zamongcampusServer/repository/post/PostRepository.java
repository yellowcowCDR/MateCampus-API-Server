package com.litCitrus.zamongcampusServer.repository.post;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    List<Post> findAllByLikedUsers_UserAndDeletedFalse(User user);
    List<Post> findAllByBookMarkUsers_UserAndDeletedFalse(User user);
    List<Post> findAllByBookMarkUsers_UserAndDeletedFalse(User user, Pageable page);

    Post findByIdAndDeletedFalse(Long id);

    List<Post> findAllByUserAndDeletedFalse(User user, Pageable page); // 게시물(작성자 기준)
    List<Post> findAllByUserAndDeletedFalseOrderByCreatedAtDesc(User user, Pageable page); // 게시물(작성자 기준 최신순)
    List<Post> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable page); // 최신순
    // 인기순: likeCount > viewCount > createDesc
    List<Post> findAllByDeletedFalseOrderByLikeCountDescViewCountDescCreatedAtDesc(Pageable page);

}
