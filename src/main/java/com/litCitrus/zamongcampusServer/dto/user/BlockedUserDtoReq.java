package com.litCitrus.zamongcampusServer.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class BlockedUserDtoReq {
    @Getter
    @Setter
    @AllArgsConstructor
    public static class Create{
        private String requested_user_id;
        private String blocked_user_id;
    }
}
