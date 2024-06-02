package com.phoenix.amazonbackend.utils;

public class ApplicationConstantsUtils {
    private ApplicationConstantsUtils(){
        // Util Class, must not be instantiated
    }
    public enum GENDER {
        MALE, FEMALE, NON_BINARY, LGBTQ
    }

    public enum DestinationDtoType {
        USER_DTO,CATEGORY_DTO
    }

    public enum USER_FIELDS {
        USER_NAME, FIRST_NAME, LAST_NAME, PRIMARY_EMAIL, SECONDARY_EMAIL, GENDER, LAST_SEEN, ABOUT, PASSWORD, PROFILE_IMAGE

    }
}
