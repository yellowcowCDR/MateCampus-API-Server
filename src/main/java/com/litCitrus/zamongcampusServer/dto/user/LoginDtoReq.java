package com.litCitrus.zamongcampusServer.dto.user;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@ToString
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDtoReq {

    @NotNull
    @Size(min = 3, max = 50)
    private String loginId;

    @NotNull
    @Size(min = 3, max = 100)
    private String password;
}
