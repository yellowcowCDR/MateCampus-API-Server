package com.litCitrus.zamongcampusServer.domain.post;

import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class PostCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private PostCategoryCode postCategoryCode;

}
