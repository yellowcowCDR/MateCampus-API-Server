package com.litCitrus.zamongcampusServer.domain.post;


import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String participantIndex;

    public static PostParticipant createPostPartcipant(User user, Post post, String participantIndex){
        final PostParticipant postParticipant = PostParticipant.builder()
                .post(post)
                .user(user)
                .participantIndex(participantIndex)
                .build();
        return postParticipant;
    }

    public boolean isAuthor(){
        return participantIndex.isEmpty();
    }
}
