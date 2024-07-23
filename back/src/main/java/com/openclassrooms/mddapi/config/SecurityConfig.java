package com.openclassrooms.mddapi.config;

import com.openclassrooms.mddapi.filter.JwtFilter;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Classe de configuration de la sécurité pour l'application
 * Cette configuration utilise JWT pour sécuriser les endpoints
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    /**
     * Bean pour le service des détails utilisateur
     * @return une implémentation de {@link UserDetailsService}
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new InfoUtilisateurServiceImpl();
    }

    /**
     * Bean pour l'encodeur de mot de passe
     * Utilise {@link BCryptPasswordEncoder} pour encoder les mots de passe
     * @return une instance de {@link PasswordEncoder}
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean pour le fournisseur d'authentification
     * Configure le fournisseur d'authentification, avec le service des détails utilisateur et l'encodeur de mot de passe
     * @return une instance de {@link AuthenticationProvider}
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * Bean pour le gestionnaire d'authentification
     * @param config la configuration d'authentiffication
     * @return une instance de {@link AuthenticationManager}
     * @throws Exception si une erreur survient lors de la création du gestionnaire d'authentification
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configuration de la chaîne de filtre de sécurité HTTP
     * Désactive CSRF, configure la politique de session pour être sans état,
     * ajoute un filtre JWT et configure les autorisation pour les requêtes HTTP
     *
     * @param http l'objet {@link HttpSecurity} à configurer
     * @return une instance de {@link SecurityFilterChain}
     * @throws Exception si une erreur survient lors de la configuration de la sécurité HTTP
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Désactive la protection CSRF car nous utilisons des tokens JWT pour l'authentification
        http.csrf(AbstractHttpConfigurer::disable);

        // Configure la gestion de la session pour être sans état (stateless)
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Configure le fournisseur d'authentification et ajoute le filtre JWT
        http.authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Configure les règles d'autorisation des requêtes HTTP
        http.authorizeHttpRequests(auth -> {
            // Autorise l'accès sans authentification aux endpoints spécifiés
            auth.requestMatchers(
                    // Inscription & Connexion
                    new AntPathRequestMatcher("/auth/welcome"),
                    new AntPathRequestMatcher("/auth/register"),
                    new AntPathRequestMatcher("/auth/login"),

                    // Liens Swagger (Documentations)
                    new AntPathRequestMatcher("/v3/api-docs/**"),
                    new AntPathRequestMatcher("/swagger-ui/**"),
                    new AntPathRequestMatcher("/swagger-ui.html")
            ).permitAll()
            .anyRequest().permitAll();
        });

        // Construit et retourne la chaîne de filtres de sécurité
        return http.build();
    }

}