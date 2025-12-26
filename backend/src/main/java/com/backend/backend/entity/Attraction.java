package com.backend.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "attractions")
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "no")
    private Integer no;

    @Column(name = "content_id")
    private Integer contentId;

    @Column(name = "title", length = 500)
    private String title;

    @Column(name = "content_type_id")
    private Integer contentTypeId;

    @Column(name = "area_code")
    private Integer areaCode;

    @Column(name = "si_gun_gu_code")
    private Integer siGunGuCode;

    @Column(name = "first_image1", length = 1000)
    private String firstImage1;

    @Column(name = "first_image2", length = 100)
    private String firstImage2;

    @Column(name = "map_level")
    private Integer mapLevel;

    @Column(name = "latitude", precision = 20, scale = 17)
    private BigDecimal latitude;

    @Column(name = "longitude", precision = 20, scale = 17)
    private BigDecimal longitude;

    @Column(name = "tel", length = 20)
    private String tel;

    @Column(name = "addr1", length = 100)
    private String addr1;

    @Column(name = "addr2", length = 100)
    private String addr2;

    @Column(name = "homepage", length = 1000)
    private String homepage;

    // 리스트 조회에서는 무거우니까 DTO에서 제외할 예정
    @Lob
    @Column(name = "overview", columnDefinition = "varchar(10000)")
    private String overview;

    protected Attraction() {} // JPA 기본 생성자
}
