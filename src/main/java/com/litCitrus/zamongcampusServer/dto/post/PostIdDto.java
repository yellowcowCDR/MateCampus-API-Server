package com.litCitrus.zamongcampusServer.dto.post;

import lombok.Getter;

import java.util.List;

@Getter
public class PostIdDto {

    List<Long> myLikePostIds;
    List<Long> myBookMarkIds;

    public PostIdDto(List<Long> myLikePostIds, List<Long> myBookMarkIds){
        this.myLikePostIds = myLikePostIds;
        this.myBookMarkIds = myBookMarkIds;
    }

}
