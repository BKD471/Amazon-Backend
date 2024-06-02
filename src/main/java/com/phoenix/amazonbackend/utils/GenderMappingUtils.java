package com.phoenix.amazonbackend.utils;

import java.util.HashMap;
import java.util.Map;

import com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.GENDER;

import static com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.GENDER.FEMALE;
import static com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.GENDER.LGBTQ;
import static com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.GENDER.MALE;
import static com.phoenix.amazonbackend.utils.ApplicationConstantsUtils.GENDER.NON_BINARY;

public class GenderMappingUtils {
    private GenderMappingUtils(){
        // Util Class, must not be instantiated
    }
    public static final Map<String, GENDER> genderMap = new HashMap<>();

    static {
        genderMap.put("MALE", MALE);
        genderMap.put("FEMALE", FEMALE);
        genderMap.put("NON_BINARY", NON_BINARY);
        genderMap.put("LGBTQ", LGBTQ);
    }

    public static GENDER getGender(final String gender) {
        if (genderMap.containsKey(gender)) return genderMap.get(gender);
        return null;
    }
}
