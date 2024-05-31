package com.phoenix.amazonbackend.utils;

import java.util.HashMap;
import java.util.Map;

import static com.phoenix.amazonbackend.utils.AllConstants.GENDER;
import static com.phoenix.amazonbackend.utils.AllConstants.GENDER.FEMALE;
import static com.phoenix.amazonbackend.utils.AllConstants.GENDER.LGBTQ;
import static com.phoenix.amazonbackend.utils.AllConstants.GENDER.MALE;
import static com.phoenix.amazonbackend.utils.AllConstants.GENDER.NON_BINARY;

public class GenderMapHelpers {
    public static final Map<String, AllConstants.GENDER> genderMap = new HashMap<>();

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
