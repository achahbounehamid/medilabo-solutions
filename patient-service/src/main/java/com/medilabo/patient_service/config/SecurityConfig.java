package com.medilabo.patient_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
/**
 * Chaîne de filtres Spring Security pour le microservice patient-service.
 *
 * <p><b>Règles d’accès :</b></p>
 * <ul>
 *   <li><code>/h2-console/**</code> : accès autorisé (utile en dev pour la console H2).</li>
 *   <li><code>/api/**</code> : accès autorisé (exposition publique des endpoints API).</li>
 *   <li><code>/actuator/health</code>, <code>/actuator/info</code> : accès autorisé (healthchecks, infos).</li>
 *   <li>Toutes les autres routes nécessitent une authentification.</li>
 * </ul>
 *
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .requestMatchers("/actuator/health", "/actuator/info").permitAll()
                        .anyRequest().authenticated()
                )
                .csrf(csrf -> csrf.disable())
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
                .build();
    }
}

