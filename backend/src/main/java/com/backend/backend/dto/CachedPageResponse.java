package com.backend.backend.dto;

import lombok.*;

import java.util.List;

/**
 * Redis 에 저장할 response
 * @param <T>
 */
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CachedPageResponse<T> {
    private List<T> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private int numberOfElements;
    private String sort; // "no,DESC" 같은 문자열로 저장(테스트용)
}
