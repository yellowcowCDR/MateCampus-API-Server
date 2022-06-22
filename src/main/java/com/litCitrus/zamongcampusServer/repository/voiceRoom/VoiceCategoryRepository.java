package com.litCitrus.zamongcampusServer.repository.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceCategory;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceCategoryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoiceCategoryRepository extends JpaRepository<VoiceCategory, Long> {

    List<VoiceCategory> findByVoiceCategoryCodeIsIn(List<VoiceCategoryCode> voiceCategoryCodeList);
}
