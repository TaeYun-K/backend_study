package com.backend.backend.service;


import com.backend.backend.dto.AttractionSummaryResponse;
import com.backend.backend.dto.CachedPageResponse;
import com.backend.backend.entity.Attraction;
import com.backend.backend.repository.AttractionRepository;
import com.backend.backend.repository.AttractionSpecs;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttractionQueryService {

    private final AttractionRepository attractionRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public Page<AttractionSummaryResponse> search(Integer areaCode,
                                                 Integer contentTypeId,
                                                 String keyword,
                                                 int page,
                                                 int size) {

        // 성능 실험용: 정렬을 명확히 (최신 no DESC)
        Sort sort = Sort.by(Sort.Direction.DESC, "no");
        Pageable pageable = PageRequest.of(page, size, sort);

        boolean cacheable = page == 0;
        String normalizedKeyword = (keyword == null || keyword.isBlank()) ? null : keyword.trim();
        String cacheKey = null;
        if (cacheable) {
            cacheKey = "attractions:page=0"
                + ":area=" + (areaCode == null ? "all" : areaCode)
                + ":ct=" + (contentTypeId == null ? "all" : contentTypeId)
                + ":kw=" + (normalizedKeyword == null ? "none" : normalizedKeyword)
                + ":size=" + size
                + ":sort=no,DESC";

            // ✅ 1) Redis 먼저 조회 (첫 페이지만)
            String cachedJson = stringRedisTemplate.opsForValue().get(cacheKey);
            if (cachedJson != null) {
                try {
                    CachedPageResponse<AttractionSummaryResponse> cached =
                        objectMapper.readValue(
                            cachedJson,
                            new TypeReference<CachedPageResponse<AttractionSummaryResponse>>() {
                            }
                        );

                    // cached -> Page로 복원
                    List<AttractionSummaryResponse> content = cached.getContent();
                    long total = cached.getTotalElements();

                    log.info("[CACHE HIT] key={}", cacheKey);
                    return new PageImpl<>(content, pageable, total);
                } catch (Exception e) {
                    // 캐시가 깨졌으면 삭제하고 DB로 fallback
                    stringRedisTemplate.delete(cacheKey);
                }
            }
        }

        // ✅ 2) DB 조회
        Specification<Attraction> spec = Specification.where(AttractionSpecs.areaCodeEq(areaCode))
            .and(AttractionSpecs.contentTypeEq(contentTypeId))
            .and(AttractionSpecs.titleContains(normalizedKeyword));

        Page<AttractionSummaryResponse> result =
            attractionRepository.findAll(spec, pageable)
                .map(AttractionSummaryResponse::from);

        // ✅ 3) Redis에 저장 (첫 페이지만, TTL + 약간의 jitter)
        if (cacheable && cacheKey != null) {
            try {
                CachedPageResponse<AttractionSummaryResponse> wrapper = CachedPageResponse.<AttractionSummaryResponse>builder()
                    .content(result.getContent())
                    .page(result.getNumber())
                    .size(result.getSize())
                    .totalElements(result.getTotalElements())
                    .totalPages(result.getTotalPages())
                    .first(result.isFirst())
                    .last(result.isLast())
                    .numberOfElements(result.getNumberOfElements())
                    .sort("no,DESC")
                    .build();

                String json = objectMapper.writeValueAsString(wrapper);

                // 테스트용 TTL 120초 + 0~20초 지터(동시 만료 방지)
                long ttlSec = 120 + ThreadLocalRandom.current().nextInt(0, 21);
                stringRedisTemplate.opsForValue().set(cacheKey, json, Duration.ofSeconds(ttlSec));
            } catch (Exception ignored) {

            }
        }
        return result;
    }
}
