package com.medilabo.front.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
/**
 * Configuration de la sécurité Spring Security pour le microservice front-service.
 *
 * <p>
 * - Déclare la chaîne de filtres de sécurité (routes publiques/protégées, login/logout).<br>
 * - Définit un utilisateur en mémoire pour l’authentification (profil de démo).<br>
 * - Expose un {@link RestTemplate} pour les appels HTTP sortants depuis le front.<br>
 * </p>
 */
@Configuration
public class SecurityConfig {
    /**
     * Chaîne de filtres de sécurité HTTP.
     *
     * <p><b>Règles d’accès :</b></p>
     * <ul>
     *   <li>Accès public : "/", "/login", ressources statiques (css/js/webjars/images/assets).</li>
     *   <li>Accès authentifié (GET) : "/homePage", "/patient/infos/**", "/notes/**".</li>
     *   <li>Accès authentifié (POST) : "/notes/**".</li>
     *   <li>Toute autre requête nécessite une authentification.</li>
     * </ul>
     *
     * <p><b>Form Login :</b></p>
     * <ul>
     *   <li>Page de connexion : "/login".</li>
     *   <li>Redirection après succès : "/homePage".</li>
     * </ul>
     *
     * <p><b>Logout :</b></p>
     * <ul>
     *   <li>URL : "/logout".</li>
     *   <li>Redirection après déconnexion : "/login?logout".</li>
     * </ul>
     *
     * @param http configuration HTTP fournie par Spring Security
     * @return la {@link SecurityFilterChain} configurée
     * @throws Exception si la construction de la configuration échoue
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/css/**", "/js/**", "/webjars/**", "/images/**", "/assets/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/homePage", "/patient/infos/**").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/patient/**/notes").authenticated()
                                .requestMatchers(HttpMethod.GET, "/notes/**").authenticated()
                                .requestMatchers(HttpMethod.POST, "/notes/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/homePage", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )
        ;
        return http.build();
    }
    /**
     * Déclare un service d’utilisateurs en mémoire (usage démo).
     *
     * <p>
     * Crée un utilisateur : login <code>user</code>, mot de passe <code>password</code>, rôle <code>USER</code>.
     * </p>
     *
     * @return un {@link UserDetailsService} en mémoire
     */
    @Bean
    public UserDetailsService users() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }
    /**
     * Expose un {@link RestTemplate} pour les appels HTTP sortants.
     *
     * @return une instance de {@link RestTemplate}
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
