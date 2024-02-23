package com.phoenix.amazon.AmazonBackend.dto.requestDtos;


public record UserCreatedRequestDto(UserDto userDto, AddressRequestDto addressRequestDto) {
    public UserCreatedRequestDto(UserDto userDto,
                                AddressRequestDto addressRequestDto) {
        this.userDto = userDto;
        this.addressRequestDto = addressRequestDto;
    }

    public static final class builder{
        private UserDto userDto;
        private AddressRequestDto addressRequestDto;

        public builder(){}

        public builder userDto(final UserDto userDto){
            this.userDto=userDto;
            return this;
        }

        public builder addressRequestDto(final AddressRequestDto addressRequestDto){
            this.addressRequestDto=addressRequestDto;
            return this;
        }

        public UserCreatedRequestDto build(){
            return new UserCreatedRequestDto(userDto,addressRequestDto);
        }
    }
}
