package com.litCitrus.zamongcampusServer.repository.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoiceRoomRepository extends JpaRepository<VoiceRoom, Long> {
}
