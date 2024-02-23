package com.phoenix.amazon.AmazonBackend.dto.responseDtos;

import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UserDto;

import java.util.Set;

public record UserCreatedResponseDto(UserDto userDto, Set<AddressResponseDto> addressResponseDto) {
    public UserCreatedResponseDto(UserDto userDto,
                                Set<AddressResponseDto> addressResponseDto) {
        this.userDto = userDto;
        this.addressResponseDto = addressResponseDto;
    }

    public static final class builder{
        private UserDto userDto;
        private Set<AddressResponseDto> addressResponseDto;

        public builder(){}

        public builder userDto(final UserDto userDto){
            this.userDto=userDto;
            return this;
        }

        public builder addressResponseDto(final Set<AddressResponseDto> addressResponseDto){
            this.addressResponseDto=addressResponseDto;
            return this;
        }

        public UserCreatedResponseDto build(){
            return new UserCreatedResponseDto(userDto,addressResponseDto);
        }
    }
}
