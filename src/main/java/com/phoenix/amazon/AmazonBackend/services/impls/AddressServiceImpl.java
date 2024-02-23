package com.phoenix.amazon.AmazonBackend.services.impls;

import com.phoenix.amazon.AmazonBackend.dto.requestDtos.AddressRequestDto;
import com.phoenix.amazon.AmazonBackend.dto.responseDtos.AddressResponseDto;
import com.phoenix.amazon.AmazonBackend.services.IAddressService;
import org.springframework.stereotype.Service;

@Service("AddressServicePrimary")
public class AddressServiceImpl implements IAddressService {
    /**
     * @param userId
     * @param userName
     * @param primaryEmail
     * @return
     */
    @Override
    public AddressResponseDto getAddressByUserIdOrUserNameOrPrimaryEmail(String userId, String userName, String primaryEmail) {
        return null;
    }

    /**
     * @param addressRequestDto
     * @param userId
     * @param userName
     * @param primaryEmail
     * @return
     */
    @Override
    public AddressResponseDto updateAddressByUserIdOrUserNameOrPrimaryEmail(AddressRequestDto addressRequestDto, String userId, String userName, String primaryEmail) {
        return null;
    }
}
