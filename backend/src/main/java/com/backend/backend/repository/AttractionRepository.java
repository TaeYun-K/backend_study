package com.backend.backend.repository;

import com.backend.backend.entity.Attraction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AttractionRepository extends JpaRepository<Attraction, Integer>,
        JpaSpecificationExecutor<Attraction> {
}
