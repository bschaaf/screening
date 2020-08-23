package nl.rockstars.config;

import javax.validation.constraints.NotBlank;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "rockstars")
public class RockstarProperties {
    @NotBlank
    private String baseUrl;
    @NotBlank
    private String songFile;
}

