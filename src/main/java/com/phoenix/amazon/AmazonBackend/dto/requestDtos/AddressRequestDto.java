package com.phoenix.amazon.AmazonBackend.dto.requestDtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import static com.phoenix.amazon.AmazonBackend.helpers.AllConstantHelpers.AddressType;

public record AddressRequestDto(String addressId,
                                @NotNull(message = "mobile number must not be null")
                                @Min(value = 1, message = "mobile number must be at least not empty")
                                String mobileNumber,

                                @NotNull(message = "address line number1 must not be null")
                                @Min(value = 1, message = "address line number1 must be at least not empty")
                                String addressLineNumberOne,

                                @NotNull(message = "address line number2 must not be null")
                                @Min(value = 1, message = "address line number2 must be at least not empty")
                                String addressLineNumberTwo,

                                @NotNull(message = "address type must not be null")
                                @Min(value = 1, message = "address type must be at least not empty")
                                AddressType addressType,

                                @NotNull(message = "address pinCode must not be null")
                                @Min(value = 1, message = "address pinCode must be at least not empty")
                                String pinCode,

                                @NotNull(message = "address town or city must not be null")
                                @Min(value = 1, message = "address town or city must be at least not empty")
                                String townOrCity) {

    public AddressRequestDto(String addressId,
                             String mobileNumber,
                             String addressLineNumberOne,
                             String addressLineNumberTwo,
                             AddressType addressType,
                             String pinCode,
                             String townOrCity) {
        this.addressId = addressId;
        this.mobileNumber = mobileNumber;
        this.addressLineNumberOne = addressLineNumberOne;
        this.addressLineNumberTwo = addressLineNumberTwo;
        this.addressType = addressType;
        this.pinCode = pinCode;
        this.townOrCity = townOrCity;
    }

    public static final class builder {
        private String addressId;
        private String mobileNumber;
        private String addressLineNumberOne;
        private String addressLineNumberTwo;
        private AddressType addressType;
        private String pinCode;
        private String townOrCity;

        public builder() {
        }

        public builder addressId(final String addressId) {
            this.addressId = addressId;
            return this;
        }

        public builder mobileNumber(final String mobileNumber) {
            this.mobileNumber = mobileNumber;
            return this;
        }

        public builder addressLineNumberOne(final String addressLineNumberOne) {
            this.addressLineNumberOne = addressLineNumberOne;
            return this;
        }

        public builder addressLineNumberTwo(final String addressLineNumberTwo) {
            this.addressLineNumberTwo = addressLineNumberTwo;
            return this;
        }

        public builder addressType(final AddressType addressType) {
            this.addressType = addressType;
            return this;
        }

        public builder pinCode(final String pinCode) {
            this.pinCode = pinCode;
            return this;
        }

        public builder townOrCity(final String townOrCity) {
            this.townOrCity = townOrCity;
            return this;
        }


        public AddressRequestDto build() {
            return new AddressRequestDto(addressId,
                    mobileNumber,
                    addressLineNumberOne,
                    addressLineNumberTwo,
                    addressType,
                    pinCode,
                    townOrCity);
        }
    }
}
