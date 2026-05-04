package com.backend.fiestaStaff.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Origen permitido: el frontend de React (Vite)
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        // Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        // Cabeceras permitidas (incluye Authorization para el JWT)
        config.setAllowedHeaders(List.of("*"));

        // Permite enviar cookies y cabeceras de autenticación
        config.setAllowCredentials(true);

        // Cuánto tiempo el navegador puede cachear la respuesta preflight (en segundos)
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
