package com.phoenix.amazonbackend.service;


import com.phoenix.amazonbackend.config.ApplicationProperties;
import com.phoenix.amazonbackend.entities.Users;
import com.phoenix.amazonbackend.repository.IUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service("ImageServicePrimary")
public class ImageServiceImpl extends AbstractService implements IImageService {
    private final IUserRepository userRepository;
    private final ApplicationProperties applicationProperties;

    public ImageServiceImpl(final IUserRepository userRepository,
                            final ApplicationProperties applicationProperties) {
        super(userRepository);
        this.userRepository = userRepository;
        this.applicationProperties=applicationProperties;
    }

    private String processImageUpload(final MultipartFile image,
                                      final String imagePath) throws IOException {

        final String originalFileName = image.getOriginalFilename();
        final String fileName = UUID.randomUUID().toString();

        // get the image extension
        final String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        // get the full image name
        final String fileNameWithExtension = fileName + extension;
        final String fullPathWithFileName = imagePath + File.separator + fileNameWithExtension;

        // check if image format is withing compatible format types
        if (extension.equalsIgnoreCase(".jpg") ||
                extension.equalsIgnoreCase(".jpeg") ||
                extension.equalsIgnoreCase(".png") ||
                extension.equalsIgnoreCase(".avif")) {
            File folder = new File(imagePath);
            if (!folder.exists()) folder.mkdirs();

            // image placed to its directory
            Files.copy(image.getInputStream(), Paths.get(fullPathWithFileName));
            return fileNameWithExtension;
        }
        return null;
    }

    /**
     * @param image        - profile image of user
     * @param userId       - userId of user
     * @param userName     - username of user
     * @param primaryEmail - primary email of user
     * @return string
     **/
    @Override
    public String uploadUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final MultipartFile image,
                                                                         final UUID userId,
                                                                         final String userName,
                                                                         final String primaryEmail) throws IOException {
        // load user from db
        final Users fetchedUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
        // upload image & get its name
        final String fileNameWithExtension = processImageUpload(image, applicationProperties.getUserProfileImagePath());

        fetchedUser.setProfileImage(fileNameWithExtension);
        //save
        userRepository.save(fetchedUser);
        return fileNameWithExtension;
    }

    @Override
    public InputStream serveUserImageServiceByUserIdOrUserNameOrPrimaryEmail(final UUID userId,
                                                                             final String userName,
                                                                             final String primaryEmail) throws IOException {
        // load user from db
        final Users oldUser = loadUserByUserIdOrUserNameOrPrimaryEmail(userId, userName, primaryEmail);
        final String fullPath = applicationProperties.getUserProfileImagePath() + File.separator + oldUser.getProfileImage();
        return new FileInputStream(fullPath);
    }
}
