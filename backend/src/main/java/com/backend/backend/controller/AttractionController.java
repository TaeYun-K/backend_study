package com.backend.backend.controller;


import com.backend.backend.dto.AttractionSummaryResponse;
import com.backend.backend.service.AttractionQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attractions")
public class AttractionController {

    private final AttractionQueryService attractionQueryService;

    // 예: /api/attractions?areaCode=1&contentTypeId=12&keyword=해수욕장&page=0&size=20
    @GetMapping
    public Page<AttractionSummaryResponse> search(
            @RequestParam(required = false) Integer areaCode,
            @RequestParam(required = false) Integer contentTypeId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return attractionQueryService.search(areaCode, contentTypeId, keyword, page, size);
    }
}
