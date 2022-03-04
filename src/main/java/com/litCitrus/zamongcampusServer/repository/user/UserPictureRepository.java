package com.litCitrus.zamongcampusServer.repository.user;

import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.user.UserPicture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPictureRepository extends JpaRepository<UserPicture, Long> {
    List<UserPicture> findAllByUser(User user);
}
