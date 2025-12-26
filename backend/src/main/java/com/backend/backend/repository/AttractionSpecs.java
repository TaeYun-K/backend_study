package com.backend.backend.repository;

import com.backend.backend.entity.Attraction;
import org.springframework.data.jpa.domain.Specification;

public class AttractionSpecs {

    public static Specification<Attraction> areaCodeEq(Integer areaCode) {
        return (root, query, cb) ->
                areaCode == null ? cb.conjunction() : cb.equal(root.get("areaCode"), areaCode);
    }

    public static Specification<Attraction> contentTypeEq(Integer contentTypeId) {
        return (root, query, cb) ->
                contentTypeId == null ? cb.conjunction() : cb.equal(root.get("contentTypeId"), contentTypeId);
    }

    public static Specification<Attraction> titleContains(String keyword) {
        return (root, query, cb) ->
                (keyword == null || keyword.isBlank()) ? cb.conjunction()
                        : cb.like(root.get("title"), "%" + keyword.trim() + "%");
    }
}
