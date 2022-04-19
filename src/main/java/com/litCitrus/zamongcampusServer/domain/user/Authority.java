package com.litCitrus.zamongcampusServer.domain.user;

import lombok.*;
import javax.persistence.*;
/// setter 삭제 필요.
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @Column(length = 50)
    private String authorityName;
}
