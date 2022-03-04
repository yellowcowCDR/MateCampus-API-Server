package com.litCitrus.zamongcampusServer.domain.user;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ID;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotEmpty
    private String stored_file_path;

    public static UserPicture createUserPicture(User user, String url){
        final UserPicture userPicture = UserPicture.builder()
                .user(user)
                .stored_file_path(url)
                .build();
        return userPicture;
    }


}
