package com.litCitrus.zamongcampusServer.domain.user;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Interest interest;

    public UserInterest(User user, Interest interest){
        this.user = user;
        this.interest = interest;
    }

    public Interest getInterest()
    {
        return interest;
    }

    public Long getInterestId() {return interest.getId();}

}
