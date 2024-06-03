package com.phoenix.amazonbackend.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.phoenix.amazon.properties")
@Getter
public class ApplicationProperties {
    private String userProfileImagePath;
}
