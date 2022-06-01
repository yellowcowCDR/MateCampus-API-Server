package com.litCitrus.zamongcampusServer.api.interest;

import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoRes;
import com.litCitrus.zamongcampusServer.service.interest.InterestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interest")
public class InterestApiController {

    private final InterestService interestService;

    @GetMapping("/my")
    public ResponseEntity<List<InterestDtoRes>> getMyInterests(){
        return ResponseEntity.ok(interestService.getMyInterests());
    }
}
