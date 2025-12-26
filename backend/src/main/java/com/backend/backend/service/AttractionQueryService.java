package com.backend.backend.service;


import com.backend.backend.dto.AttractionSummaryResponse;
import com.backend.backend.entity.Attraction;
import com.backend.backend.repository.AttractionRepository;
import com.backend.backend.repository.AttractionSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AttractionQueryService {

    private final AttractionRepository attractionRepository;

    @Transactional(readOnly = true)
    public Page<AttractionSummaryResponse> search(Integer areaCode,
                                                 Integer contentTypeId,
                                                 String keyword,
                                                 int page,
                                                 int size) {

        Specification<Attraction> spec = Specification.where(AttractionSpecs.areaCodeEq(areaCode))
                .and(AttractionSpecs.contentTypeEq(contentTypeId))
                .and(AttractionSpecs.titleContains(keyword));

        // 성능 실험용: 정렬을 명확히 (최신 no DESC)
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "no"));

        return attractionRepository.findAll(spec, pageable)
                .map(AttractionSummaryResponse::from);
    }
}
