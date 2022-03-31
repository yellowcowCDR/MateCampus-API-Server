package com.litCitrus.zamongcampusServer.io.agora;

import com.litCitrus.zamongcampusServer.io.agora.media.RtcTokenBuilder;
import org.springframework.stereotype.Component;

@Component
public class AgoraHandler {

    public String getRTCToken(AgoraRepository resource) {

        RtcTokenBuilder token = new RtcTokenBuilder();
        String channelName = resource.getChannelName();
        int expireTime = resource.getExpirationTimeInSeconds();
        RtcTokenBuilder.Role role = RtcTokenBuilder.Role.Role_Subscriber;
        int uid = resource.getUid();

        /** check for null channelName */
        if (channelName==null){
            throw new IllegalStateException();
        }

        if(expireTime==0){
            expireTime = 3600;
        }

        if(resource.getRole()==1){
            role = RtcTokenBuilder.Role.Role_Publisher;
        }else if(resource.getRole()==0){
            role = RtcTokenBuilder.Role.Role_Attendee;
        }
        /** check for null channelName */

        int timestamp = (int)(System.currentTimeMillis() / 1000 + expireTime);


        String result = token.buildTokenWithUid(resource.appId, resource.appCertificate,
                channelName, uid, role, timestamp);
        System.out.print(result);
        return result;


    }
}
