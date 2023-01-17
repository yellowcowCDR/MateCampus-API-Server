package com.litCitrus.zamongcampusServer.dto.post;

import com.litCitrus.zamongcampusServer.domain.user.CollegeCode;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSearch {
    private User loggedUser = SecurityUtil.getUser();
    private User writer;
    private CollegeCode collegeCode;

    public PostSearch(User writer, CollegeCode collegeCode) {
        this.writer = writer;
        this.collegeCode = collegeCode;
    }
}
