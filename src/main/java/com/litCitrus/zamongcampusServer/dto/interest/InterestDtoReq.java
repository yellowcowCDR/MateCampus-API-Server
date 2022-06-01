package com.litCitrus.zamongcampusServer.dto.interest;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class InterestDtoReq {

    @NotBlank(message = "interestCodesㄱㅏ null일 수 없다")
    private String interestCode;
}
