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

//    List<Post> findAllByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
//    List<Post> findAllByDeletedFalseOrderByLikedUsersDesc(Pageable pageable);
//    List<Post> findByUserAndDeletedFalseOrderByCreatedAtDesc(User user, Pageable pageable);

    List<Post> findByUser(User user, Pageable page);


    @Query(value = "SELECT * From post where deleted = 0", nativeQuery = true)
    List<Post> findAllByOrderByCreatedAtDesc(Pageable page);
}
