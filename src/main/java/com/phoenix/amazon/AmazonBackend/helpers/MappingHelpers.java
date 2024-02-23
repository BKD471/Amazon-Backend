package com.phoenix.amazon.AmazonBackend.helpers;

import com.phoenix.amazon.AmazonBackend.dto.requestDtos.AddressRequestDto;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.CategoryDto;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.ProductDto;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UpdateUserDto;
import com.phoenix.amazon.AmazonBackend.dto.requestDtos.UserDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.AddressResponseDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.UserCreatedResponseDto;
import com.phoenix.amazon.AmazonBackend.entity.Address;
import com.phoenix.amazon.AmazonBackend.entity.Category;
import com.phoenix.amazon.AmazonBackend.entity.Product;
import com.phoenix.amazon.AmazonBackend.entity.Users;

import java.util.Set;
import java.util.stream.Collectors;

import static com.phoenix.amazon.AmazonBackend.helpers.GenderMapHelpers.getGender;

public class MappingHelpers{
    public static UserDto UsersToUsersDto(final Users users) {
        return new UserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .profileImage(users.getProfileImage())
                .about(users.getAbout())
                .lastSeen(users.getLastSeen())
                .build();
    }

    public static Users UserDtoToUsers(final UserDto userDto) {
        return new Users.builder()
                .userId(userDto.userId())
                .userName(userDto.userName())
                .firstName(userDto.firstName())
                .lastName(userDto.lastName())
                .primaryEmail(userDto.primaryEmail())
                .secondaryEmail(userDto.secondaryEmail())
                .password(userDto.password())
                .gender(getGender(userDto.gender()))
                .profileImage(userDto.profileImage())
                .about(userDto.about())
                .build();
    }

    public static Users UserUpdateDtoToUsers(final UpdateUserDto updateUserDto) {
        return new Users.builder()
                .userId(updateUserDto.userId())
                .userName(updateUserDto.userName())
                .firstName(updateUserDto.firstName())
                .lastName(updateUserDto.lastName())
                .primaryEmail(updateUserDto.primaryEmail())
                .secondaryEmail(updateUserDto.secondaryEmail())
                .gender(getGender(updateUserDto.gender()))
                .about(updateUserDto.about())
                .build();
    }

    public static UpdateUserDto UserToUpdateUserDto(final Users users) {
        return new UpdateUserDto.builder()
                .userId(users.getUserId())
                .userName(users.getUserName())
                .firstName(users.getFirstName())
                .lastName(users.getLastName())
                .primaryEmail(users.getPrimaryEmail())
                .secondaryEmail(users.getSecondaryEmail())
                .gender(users.getGender().toString())
                .about(users.getAbout())
                .build();
    }

    public static Product productDtoToProduct(final ProductDto productDto){
        return new Product.builder()
                .productId(productDto.productId())
                .title(productDto.title())
                .description(productDto.description())
                .price(productDto.price())
                .quantity(productDto.quantity())
                .addedDate(productDto.addedDate())
                .stock(productDto.stock())
                .build();
    }

    public static ProductDto productToProductDto(final Product product){
        return new ProductDto.builder()
                .productId(product.getProductId())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantity(product.getQuantity())
                .addedDate(product.getAddedDate())
                .stock(product.getStock())
                .build();
    }

    public static Category CategoryDtoToCategory(final CategoryDto categoryDto){
        return new Category.builder()
                .categoryId(categoryDto.categoryId())
                .title(categoryDto.title())
                .description(categoryDto.description())
                .build();
    }

    public static CategoryDto categoryToCategoryDto(final Category category){
        return new CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .title(category.getTitle())
                .description(category.getDescription())
                .coverImage(category.getCoverImage())
                .build();
    }

    public static Address AddressRequestDtoToAddress(final AddressRequestDto addressRequestDto){
        return new Address.builder()
                .addressId(addressRequestDto.addressId())
                .mobileNumber(addressRequestDto.mobileNumber())
                .addressLineNumberOne(addressRequestDto.addressLineNumberOne())
                .addressLineNumberTwo(addressRequestDto.addressLineNumberTwo())
                .addressType(addressRequestDto.addressType())
                .pinCode(addressRequestDto.pinCode())
                .townOrCity(addressRequestDto.townOrCity())
                .build();
    }

    public static AddressResponseDto AddressToAddressRequestDto(final Address address){
        return new AddressResponseDto.builder()
                .addressId(address.getAddressId())
                .mobileNumber(address.getMobileNumber())
                .addressLineNumberOne(address.getAddressLineNumberOne())
                .addressLineNumberTwo(address.getAddressLineNumberTwo())
                .pinCode(address.getPinCode())
                .addressType(address.getAddressType())
                .townOrCity(address.getTownOrCity())
                .district(address.getDistrict())
                .state(address.getState())
                .country(address.getCountry())
                .build();
    }

    public static AddressResponseDto AddressToAddressResponseDto(final Address address){
        return new AddressResponseDto.builder()
                .addressId(address.getAddressId())
                .mobileNumber(address.getMobileNumber())
                .addressLineNumberOne(address.getAddressLineNumberOne())
                .addressLineNumberTwo(address.getAddressLineNumberTwo())
                .pinCode(address.getPinCode())
                .addressType(address.getAddressType())
                .townOrCity(address.getTownOrCity())
                .district(address.getDistrict())
                .state(address.getState())
                .country(address.getCountry())
                .build();
    }

    public static  Address AddressResponseDtoToAddress(final AddressResponseDto addressResponseDto,final Users users){
        return new Address.builder()
                .addressId(addressResponseDto.addressId())
                .mobileNumber(addressResponseDto.mobileNumber())
                .addressLineNumberOne(addressResponseDto.addressLineNumberOne())
                .addressLineNumberTwo(addressResponseDto.addressLineNumberTwo())
                .pinCode(addressResponseDto.pinCode())
                .addressType(addressResponseDto.addressType())
                .townOrCity(addressResponseDto.townOrCity())
                .latitude(addressResponseDto.latitude())
                .longitude(addressResponseDto.longitude())
                .district(addressResponseDto.district())
                .state(addressResponseDto.state())
                .country(addressResponseDto.country())
                .users(users)
                .build();
    }

    public static UserCreatedResponseDto  UserToUserCreatedResponseDto(final Users users){
        final Set<Address> address=users.getAddress_set();
        final Set<AddressResponseDto> addressResponseDtoSet=address.stream().
                map(MappingHelpers::AddressToAddressResponseDto)
                .collect(Collectors.toSet());

        return new UserCreatedResponseDto.builder()
                .userDto(UsersToUsersDto(users))
                .addressResponseDto(addressResponseDtoSet)
                .build();
    }
}
