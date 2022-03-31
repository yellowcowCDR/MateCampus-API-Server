package com.litCitrus.zamongcampusServer.repository.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
