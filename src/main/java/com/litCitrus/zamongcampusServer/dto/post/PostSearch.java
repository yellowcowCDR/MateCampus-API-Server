package com.litCitrus.zamongcampusServer.dto.post;

import com.litCitrus.zamongcampusServer.domain.user.CollegeCode;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostSearch {
    private User loggedUser;
    private CollegeCode collegeCode;
}
