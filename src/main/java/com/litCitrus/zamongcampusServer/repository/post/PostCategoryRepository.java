package com.litCitrus.zamongcampusServer.repository.post;

import com.litCitrus.zamongcampusServer.domain.post.PostCategory;
import com.litCitrus.zamongcampusServer.domain.post.PostCategoryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCategoryRepository extends JpaRepository<PostCategory, Long> {
    List<PostCategory> findByPostCategoryCodeIsIn(List<PostCategoryCode> postCategoryCodes);
}
