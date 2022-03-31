package com.litCitrus.zamongcampusServer.service.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.voiceRoom.VoiceRoomDtoReq;
import com.litCitrus.zamongcampusServer.dto.voiceRoom.VoiceRoomDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.io.agora.AgoraHandler;
import com.litCitrus.zamongcampusServer.io.agora.AgoraRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.ParticipantRepository;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.VoiceRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class VoiceRoomService {

    private final VoiceRoomRepository voiceRoomRepository;
    private final ParticipantRepository participantRepository;
    private final UserRepository userRepository;
    private final AgoraHandler agoraHandler;

    public VoiceRoomDtoRes.Res createVoiceRoom(
             VoiceRoomDtoReq.Create dto) {
        User user = userRepository.findByLoginId(dto.getLoginId()).orElseThrow(UserNotFoundException::new);
        Participant participant = Participant.CreateParticipant(Arrays.asList(user));
        // 방 만들고 token 값 가져오기!
        // 여기서 channel 네임이 roomID 개념이 되지 않을까 싶다.
        // 현재 roomID는 모델에서 만들고 있어서 흠..
        VoiceRoom voiceRoom = VoiceRoom.createVoiceRoom(user, dto, participant);
        String token = agoraHandler.getRTCToken(new AgoraRepository(voiceRoom.getRoomId(), 0, 3600, 2));


        participantRepository.save(participant);
        voiceRoomRepository.save(voiceRoom);
        return new VoiceRoomDtoRes.Res(voiceRoom, token);
    }

//    public List<PostDtoRes.Res> getVoiceRooms(String loginId){
//        List<Post> posts = postService.getAllPostOrderbyRecent(loginId);
//        return posts.stream().map(post -> new PostDtoRes.Res(post)).collect(Collectors.toList());
//    }
//
    @Transactional
    public VoiceRoomDtoRes.Res getVoiceRoom(Long voiceRoomId, String loginId){
        User user = userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
        VoiceRoom voiceRoom = voiceRoomRepository.findById(voiceRoomId).get();
        String token = agoraHandler.getRTCToken(new AgoraRepository(voiceRoom.getRoomId(), 0, 3600, 2));
        voiceRoom.getParticipant().addUser(user);

        return new VoiceRoomDtoRes.Res(voiceRoom, token);
    }

}
