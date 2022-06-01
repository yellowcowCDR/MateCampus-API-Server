package com.litCitrus.zamongcampusServer.api.user;

import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoReq;
import com.litCitrus.zamongcampusServer.service.user.UserInterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/userInterest")
public class UserInterestApiController {

    private final UserInterestService userInterestService;

    @PutMapping("/my")
    public ResponseEntity<?> updateMyInterests(@Valid @RequestBody List<InterestDtoReq> interestDtoReqList){
        int interestCount = userInterestService.updateMyInterests(interestDtoReqList);
        return ResponseEntity.ok(Integer.toString(interestCount));
    }
}
