package com.litCitrus.zamongcampusServer.io.agora;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AgoraRepository {
    static String appId = "1db42f592687465e9ad1564ae4b55221"; // 고정
    static String appCertificate = "586fd80696fc4c89b02dc40c1926838c"; // 고정
    private String channelName; // 변경될지도. roomId.
    private int uid = 0;
    private int expirationTimeInSeconds = 3600;
    private int role = 2; // By default subscriber

}
