package com.phoenix.amazonbackend.dtos.responseDtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
public class PageableResponse<T> {
    private final List<T> content;
    private final int pageNumber;
    private final int pageSize;
    private final int totalPages;
    private final long totalElements;
    private final boolean isLastPage;
}
