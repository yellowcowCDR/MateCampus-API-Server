package com.litCitrus.zamongcampusServer.domain.interest;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import com.litCitrus.zamongcampusServer.domain.post.PostCategoryCode;
import com.litCitrus.zamongcampusServer.domain.user.UserInterest;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceCategoryCode;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Interest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @NotNull
    @Column(unique = true)
    private InterestCode interestCode;

    @OneToMany(mappedBy = "interest")
    private Set<UserInterest> userInterests;

    public static Interest createInterest(String interestCode){
        final Interest interest = Interest.builder()
                .interestCode(InterestCode.valueOf(interestCode))
                .build();
        return interest;
    }

    public static List<Interest> createInterests(List<String> interestCodes){
        return interestCodes.stream().map(interestCode -> Interest.createInterest(interestCode)).collect(Collectors.toList());
    }

}
