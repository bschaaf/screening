package nl.rockstars.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
public class SpringDataSourceProperties {
    @NotBlank
    private String url;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}

