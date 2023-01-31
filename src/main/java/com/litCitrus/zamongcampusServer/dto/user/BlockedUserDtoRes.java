package com.litCitrus.zamongcampusServer.dto.user;

import com.litCitrus.zamongcampusServer.domain.user.UserPicture;
import lombok.Getter;

import java.util.List;

public class BlockedUserDtoRes {
    @Getter
    public static class Res {
        private Long id;
        private String loginId;
        private String nickname;
        private String collegeName;
        private String majorName;
        private String imageUrl;
        private String requestedUserLoginId;

        public Res(Long id, String loginId, String nickname, String collegeName, String majorName, List<UserPicture> pictures, String requestedUserLoginId){
            this.id = id;
            this.loginId = loginId;
            this.nickname = nickname;
            this.collegeName = collegeName;
            this.majorName = majorName;
            this.imageUrl  = pictures.get(0).getStored_file_path()==null? "":pictures.get(0).getStored_file_path();
            this.requestedUserLoginId = requestedUserLoginId;
        }
    }
}
