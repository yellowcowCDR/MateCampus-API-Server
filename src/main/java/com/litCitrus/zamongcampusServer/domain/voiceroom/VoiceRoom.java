package com.litCitrus.zamongcampusServer.domain.voiceroom;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class VoiceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String roomId;

    @NonNull
    @OneToOne
    private Participant participant;

}
