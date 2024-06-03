package com.phoenix.amazonbackend.utils;


import com.phoenix.amazonbackend.dtos.responseDtos.PageableResponse;
import com.phoenix.amazonbackend.entities.Users;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.phoenix.amazonbackend.dtos.requestDtos.UserDto.usersToUsersDto;
import static com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.DestinationDtoType;


public class PagingUtils {
    private PagingUtils(){
        // Util Class, must not be instantiated
    }
    public static <U, V> PageableResponse<U> getPageableResponse(final Page<V> page, final DestinationDtoType destinationDtoType) {
        List<V> entityList = page.getContent();
        List<U> dtoList = new ArrayList<>();

        switch (destinationDtoType) {
            case USER_DTO -> {
                if (!entityList.isEmpty() && entityList.getFirst() instanceof Users) {
                    dtoList = (List<U>) entityList.stream()
                            .map(object -> usersToUsersDto((Users) object))
                            .collect(Collectors.toList());
                }
            }
        }

        return PageableResponse.<U>builder()
                .content(dtoList)
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .isLastPage(page.isLast())
                .build();
    }
}
