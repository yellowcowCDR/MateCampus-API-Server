package com.litCitrus.zamongcampusServer.domain.user;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import com.litCitrus.zamongcampusServer.domain.notification.Notification;
import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostBookMark;
import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE user SET deleted = true WHERE id=?")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 50)
    private String loginId;

    @Column(length = 100)
    private String password;

    @Column(unique = true, length = 50)
    private String nickname;

//    @Column(unique = true)
    private String deviceToken;

    private CollegeCode collegeCode;
    private MajorCode majorCode;
    private String introduction;
    private boolean activated;
    private String studentIdImageUrl;
    private Integer grade;
    private boolean gender;
    private LocalDate birth;

    @Builder.Default
    private boolean emailAuthentication = Boolean.FALSE;

    @Builder.Default
    @NotNull
    private boolean deleted = Boolean.FALSE;

    @ManyToMany
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @OneToMany(mappedBy = "user")
    private Set<UserInterest> userInterests;

    @OneToMany(mappedBy = "user")
    private Set<PostLike> likedPosts;

    @OneToMany(mappedBy = "user")
    private Set<PostBookMark> bookmarkPosts;

    @OneToMany(mappedBy = "user")
    private Set<Post> posts;

    @OneToMany(mappedBy = "user")
    private Set<PostComment> comments;

    @OneToMany(mappedBy = "user")
    private List<UserPicture> pictures;

    @OneToMany(mappedBy = "owner")
    private Set<VoiceRoom> voiceRooms;

    @OneToMany(mappedBy = "user")
    private List<ModifiedChatInfo> modifiedChatInfos;

    @OneToMany(mappedBy = "recipient")
    private List<RecommendUser> recommendUsers;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;


    public static User createUser(UserDtoReq.Create userDto, String encodedPassword, Authority authority) {
        //빌더 객체를 사용할 경우
        final User user = User.builder()
                .loginId(userDto.getLoginId())
                .password(encodedPassword)
                .authorities(Collections.singleton(authority))
                .deviceToken(userDto.getDeviceToken())
                .nickname(userDto.getNickname())
                .collegeCode(CollegeCode.valueOf(userDto.getCollegeCode()))
                .majorCode(MajorCode.valueOf(userDto.getMajorCode()))
                .introduction(userDto.getIntroduce())
                .grade(userDto.getGrade())
                .gender(userDto.getGender())
                .birth(userDto.getBirth())
//                .activated(true)  // 이거 활성화시키면 회원가입만 하면 우리 서비스 바로 사용 가능.
                .build();
        return user;
    }

    public static User createAdmin(UserDtoReq.Create userDto, String encodedPassword, List<Authority> authorities) {
        //빌더 객체를 사용할 경우
        final User user = User.builder()
                .loginId(userDto.getLoginId())
                .password(encodedPassword)
                .authorities(new HashSet<>(authorities))
                .deviceToken(userDto.getDeviceToken())
                .nickname(userDto.getNickname())
                .collegeCode(CollegeCode.valueOf(userDto.getCollegeCode()))
                .majorCode(MajorCode.valueOf(userDto.getMajorCode()))
                .introduction(userDto.getIntroduce())
//                .activated(true)  // 이거 활성화시키면 회원가입만 하면 우리 서비스 바로 사용 가능.
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

    public void updateNickname(String nickname){ this.nickname = nickname; }
    public void updateIntroduction(String introduction) { this.introduction = introduction;}
    public void updateDeviceToken(String deviceToken){ this.deviceToken = deviceToken;}
    public void updateActivated(boolean value){ this.activated = value; }
    public void setStudentIdImageUrl(String url){
        this.studentIdImageUrl = url;
    }

}
