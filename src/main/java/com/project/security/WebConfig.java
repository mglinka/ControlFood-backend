package com.project.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:5173", "https://orange-rock-03ed9c003.5.azurestaticapps.net")  // Zezwolenie na zapytania z tych domen
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")  // Określenie metod
                .allowedHeaders("Content-Type", "Authorization", "X-Requested-With", "Accept")  // Dozwolone nagłówki
                .allowCredentials(true)  // Zezwolenie na przesyłanie ciasteczek i danych uwierzytelniających
                .maxAge(3600);  // Czas przechowywania nagłówków CORS (w sekundach)
    }
}

