package com.project.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;


@Configuration
@PropertySources({
        @PropertySource("classpath:application.yml"),

})
@Getter
public class ConfigurationProperties {

    //@Value("${jwt.expiration}")
    private final Integer jwtExpiration = 900000;
}
