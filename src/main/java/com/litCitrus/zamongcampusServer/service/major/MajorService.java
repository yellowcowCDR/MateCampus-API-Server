package com.litCitrus.zamongcampusServer.service.major;

import com.litCitrus.zamongcampusServer.domain.major.Major;
import com.litCitrus.zamongcampusServer.repository.major.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MajorService {

    private final MajorApiService majorApiService;
    private final MajorRepository majorRepository;

    public Major findByNameAndSeq(long seq, String name) {
        return majorRepository.findByMajorSeqAndName(seq, name).orElseGet(() -> {
            //DB에서 검색 안 되면 OpenApi에서 검색
            Major major = majorApiService.searchMajor(seq, name).orElseThrow(NullPointerException::new);
            majorRepository.save(major);
            return major;
        });
    }
}
