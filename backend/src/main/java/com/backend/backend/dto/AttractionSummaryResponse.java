package com.backend.backend.dto;

import com.backend.backend.entity.Attraction;
import java.math.BigDecimal;

public record AttractionSummaryResponse(
        Integer no,
        String title,
        Integer contentTypeId,
        Integer areaCode,
        Integer siGunGuCode,
        String firstImage1,
        String addr1,
        BigDecimal latitude,
        BigDecimal longitude
) {
    public static AttractionSummaryResponse from(Attraction a) {
        return new AttractionSummaryResponse(
                a.getNo(),
                a.getTitle(),
                a.getContentTypeId(),
                a.getAreaCode(),
                a.getSiGunGuCode(),
                a.getFirstImage1(),
                a.getAddr1(),
                a.getLatitude(),
                a.getLongitude()
        );
    }
}
