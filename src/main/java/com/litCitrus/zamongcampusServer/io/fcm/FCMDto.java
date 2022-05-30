package com.litCitrus.zamongcampusServer.io.fcm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@Getter
@RequiredArgsConstructor
public class FCMDto {
    private final String body;
    private final Map<String, String> data;

    public FCMDto(String body){
        this.body = body;
        this.data = null;
    }
}
