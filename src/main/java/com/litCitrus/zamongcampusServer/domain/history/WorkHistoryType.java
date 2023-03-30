package com.litCitrus.zamongcampusServer.domain.history;

public class WorkHistoryType {
    public enum FunctionType{
        FEED
        ,FEED_LIKE
        ,FEED_REPLY
        ,FEED_REPLY_LIKE
        ,FEED_WRITE
        ,FEED_LIKE_LIST_TO_PROFILE
        ,FEED_TO_PROFILE
        ,LOGIN
        ,LOGOUT
        ,PROFILE_PHOTO
        ,CHAT_ROOM
        ,MESSAGE
    }
    public enum WorkType{
        VISIT
        ,WRITE
        ,SEARCH
        ,MODIFY
        ,DELETE
        ,CLICK
    }
}
