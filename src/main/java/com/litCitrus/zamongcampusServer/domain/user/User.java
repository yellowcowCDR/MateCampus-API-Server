package com.litCitrus.zamongcampusServer.domain.user;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
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

    @Column(unique = true, length = 50)
    private String loginId;

    @Column(length = 100)
    private String password;

    @Column(unique = true)
    private String deviceToken;

    @Column(unique = true)
    private String email;

    @Column(unique = true, length = 50)
    private String nickname;

    @ManyToMany
    private Set<Authority> authorities;

    private boolean activated;

    private String name;
    private String collegeCode;
    private String department;
    private int studentNum;

    @Builder.Default
    private boolean emailAuthentication = Boolean.FALSE;

    @OneToMany(mappedBy = "user")
    private Set<UserInterest> userInterests;

    @OneToMany(mappedBy = "user")
    private Set<PostLike> likedPosts;

    @OneToMany(mappedBy = "user")
    @Builder.Default
    private List<UserPicture> pictures = new ArrayList<UserPicture>();

    @OneToMany(mappedBy = "owner")
    private Set<VoiceRoom> voiceRooms;

    @OneToMany(mappedBy = "user")
    private List<ModifiedChatInfo> modifiedChatInfos;

    @Builder.Default
    @NotNull
    private boolean deleted = Boolean.FALSE;

    public static User createUser(UserDtoReq.Create userDto, String encodedPassword, Authority authority) {
        //빌더 객체를 사용할 경우
        final User user = User.builder()
                .loginId(userDto.getLoginId())
                .password(encodedPassword)
                .authorities(Collections.singleton(authority))
                .deviceToken(userDto.getDeviceToken())
                .email(userDto.getEmail())
                .nickname(userDto.getNickname())
                .name(userDto.getName())
                .collegeCode(userDto.getCollegeCode())
                .department(userDto.getDepartment())
                .studentNum(userDto.getStudentNum())
                .build();
        return user;
    }

    private static String sha256(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /** 아래 update 함수랑 합쳐야할지도. */
    public User updateUserInterests(Set<UserInterest> userInterests) {
        this.userInterests = userInterests;
        return this;
    }

    public User addUserPictures(List<UserPicture> userPictures){
        Collections.addAll(this.pictures, userPictures.toArray(new UserPicture[0]));
        return this;
    }
}
