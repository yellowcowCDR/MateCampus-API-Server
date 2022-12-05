package com.litCitrus.zamongcampusServer.dto.user;

import com.litCitrus.zamongcampusServer.domain.user.CollegeCode;
import com.litCitrus.zamongcampusServer.domain.user.UserPicture;
import lombok.Getter;

import java.util.List;

public class BlockedUserDtoRes {
    @Getter
    public static class Res {
        private Long id;
        private String loginId;
        private String nickname;
        private CollegeCode collegeCode;
        private String majorName;
        private String imageUrl;
        private String requestedUserLoginId;

        public Res(Long id, String loginId, String nickname, CollegeCode collegeCode, String majorName, List<UserPicture> pictures, String requestedUserLoginId){
            this.id = id;
            this.loginId = loginId;
            this.nickname = nickname;
            this.collegeCode = collegeCode;
            this.majorName = majorName;
            this.imageUrl  = pictures.get(0).getStored_file_path();
            this.requestedUserLoginId = requestedUserLoginId;
        }
    }
}
