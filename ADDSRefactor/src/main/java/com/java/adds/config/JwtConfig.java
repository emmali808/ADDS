package com.java.adds.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * JSON Web Token Configuration Properties
 * @author QXL
 */
@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secretKey;
    private String claimKeyAuth;
    private String tokenType;
    private String header;
    private Duration expireTime;
}
