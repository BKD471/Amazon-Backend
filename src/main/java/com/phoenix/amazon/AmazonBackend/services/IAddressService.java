package com.phoenix.amazon.AmazonBackend.services;

import com.phoenix.amazon.AmazonBackend.dto.requestDtos.AddressRequestDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.AddressResponseDto;

public interface IAddressService {
    AddressResponseDto getAddressByUserIdOrUserNameOrPrimaryEmail(final String userId,
                                                                  final String userName,
                                                                  final String primaryEmail);

    AddressResponseDto updateAddressByUserIdOrUserNameOrPrimaryEmail(final AddressRequestDto addressRequestDto,
                                                                     final String userId,
                                                                     final String userName,
                                                                     final String primaryEmail);
}
