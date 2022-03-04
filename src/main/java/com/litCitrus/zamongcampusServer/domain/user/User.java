package com.litCitrus.zamongcampusServer.domain.user;

import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id=?")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String department;

    private String collegeNumber;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String nickname;

    @Column(unique = true)
    private String loginId;

    private String password;

//    @OneToMany(mappedBy = "user")
//    private Set<UserInterest> userInterests;

    @OneToMany(mappedBy = "user")
    private Set<PostLike> likedPosts;

//    @OneToMany(mappedBy = "user")
//    private Set<PostBookMark> bookMarkPosts;

    // TODO : 반드시 unique로 변경해야함
//	@Column(unique = true)
    private String deviceToken;

    @Builder.Default
    private boolean emailAuthentication = Boolean.FALSE;

    @OneToMany(mappedBy = "user")
    private List<UserPicture> pictures;

//    @OneToMany(mappedBy = "user")
//    private Set<Keyword> keywords;

    @OneToOne(mappedBy = "user")
    private SignUpToken signUpToken;

//    @OneToMany(mappedBy = "user")
//    private List<ModifiedChatInfo> modifiedChatInfos;

    @Builder.Default
    @NotNull
    private boolean deleted = Boolean.FALSE;
}
