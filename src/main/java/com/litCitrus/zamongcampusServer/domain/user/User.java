package com.litCitrus.zamongcampusServer.domain.user;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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
    private Set<Post> posts;

    @OneToMany(mappedBy = "user")
    private Set<PostComment> comments;

    @OneToMany(mappedBy = "user")
    private List<UserPicture> pictures;

    @OneToMany(mappedBy = "owner")
    private Set<VoiceRoom> voiceRooms;

    @OneToMany(mappedBy = "user")
    private List<ModifiedChatInfo> modifiedChatInfos;


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

    public void updateUserNickname(String nickname){
        this.nickname = nickname;
    }

    public void updateDeviceToken(String deviceToken){ this.deviceToken = deviceToken;}

    /** 사용자와 같은 채팅방에 있는 사람의 프로필 변경 여부 추가 */
    // TODO: 이거 잘 저장되는지 볼 것. 아마 안될껄?
    // 안되면 아마도 해당 코드에서 ModifiedInfoRepo에 저장하는 방식으로 변경해야할 듯.
    public void addModifiedChatInfo(ModifiedChatInfo modifiedChatInfo){
        this.modifiedChatInfos.add(modifiedChatInfo);
    }

    public void setActivated(){
        this.activated = true;
    }

    // TODO: 이 함수는 따로 필요 없어 보이거든??
    public void addPicture(UserPicture userPicture){
        if(this.pictures == null){
            this.pictures = new ArrayList<>(Arrays.asList(userPicture));
        }else{
            Collections.addAll(this.pictures, userPicture);
        }
    }

    public void setStudentIdImageUrl(String url){
        this.studentIdImageUrl = url;
    }

}
