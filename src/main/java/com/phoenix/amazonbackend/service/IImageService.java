package com.phoenix.amazonbackend.service;


import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public interface IImageService {
    /**
     * Handles Client request to upload user profile image either by UserId Or UserName or PrimaryEmail
     *
     * @param image        - profile image of user
     * @param userId       - userId of user
     * @param userName     - username of user
     * @param primaryEmail - primary email of user
     * @return String
     **/
    String uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final MultipartFile image,
                                                                  final UUID userId,
                                                                  final String userName,
                                                                  final String primaryEmail) throws IOException;


    /**
     * Handles Client request to serve user profile Image either by UserId or UserName or PrimaryEmail
     *
     * @param userId       - userId of users
     * @param userName     - username of user
     * @param primaryEmail - primary email of user
     * @return InputStream
     **/
    InputStream serveUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final UUID userId,
                                                                      final String userName,
                                                                      final String primaryEmail) throws IOException;
}
