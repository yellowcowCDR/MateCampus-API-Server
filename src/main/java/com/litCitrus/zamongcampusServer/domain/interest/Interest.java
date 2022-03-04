package com.litCitrus.zamongcampusServer.domain.interest;

import com.litCitrus.zamongcampusServer.domain.BaseEntity;
import com.litCitrus.zamongcampusServer.domain.user.UserInterest;
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
public class Interest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(unique = true)
    private String title;

    @OneToMany(mappedBy = "interest")
    private Set<UserInterest> userInterests;

    public static Interest createInterest(String title){
        final Interest interest = Interest.builder()
                .title(title)
                .build();
        return interest;
    }

    public static List<Interest> createInterests(List<String> titles){
        return titles.stream().map(title -> Interest.createInterest(title)).collect(Collectors.toList());
    }

}
