package com.github.mtahasahin.evently.security.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "security.jwt")
public class JwtConfig {
    private String Uri;
    private int accessTokenExpiration;
    private int refreshTokenExpiration;
    private String secret;
}