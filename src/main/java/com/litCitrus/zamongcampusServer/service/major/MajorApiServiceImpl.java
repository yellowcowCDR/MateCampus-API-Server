package com.litCitrus.zamongcampusServer.service.major;

import com.litCitrus.zamongcampusServer.domain.major.Major;
import com.litCitrus.zamongcampusServer.repository.major.MajorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MajorApiServiceImpl implements MajorApiService {

    private final MajorRepository majorRepository;

    @Override
    public Optional<Major> searchMajor(int majorSeq, String mName) {

        return Optional.empty();
    }
}
