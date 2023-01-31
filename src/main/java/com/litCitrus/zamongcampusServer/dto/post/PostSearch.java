package com.litCitrus.zamongcampusServer.dto.post;

import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.Getter;

@Getter
public class PostSearch {
    private final User loggedUser = SecurityUtil.getUser();
    private final User writer;
    private final String collegeName;
    //첫 페이지일 경우 null
    private final Long oldestPost;
    private int pageSize = 10;

    public PostSearch(User writer, String collegeName, Long oldestPost) {
        this.writer = writer;
        this.collegeName = collegeName;
        this.oldestPost = oldestPost;
    }
}
