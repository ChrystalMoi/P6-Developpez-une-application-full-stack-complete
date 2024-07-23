package com.openclassrooms.mddapi.filter;

import com.openclassrooms.mddapi.service.InfoUtilisateurService;
import com.openclassrooms.mddapi.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Filtre pour l'authentification baser sur les tokens JWT
 * Ce filtre vérifie la présence d'un token JWT dans l'en-tête (Authorization) de la requête HTPP
 *
 * Si un token valide est trouvé → il charge les détails de l'utilisateur associé
 * et configure l'authentification dans le contexte de sécurité
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private InfoUtilisateurService infoUtilisateurService;

    /**
     * Méthode principale de filtrage des requêtes HTTP
     * Vérifie la présence et la validité du token JWT dans l'en-tête Authorization
     * Charge les détails de l'utilisateur et configure l'authentification si le token est valide
     *
     * @param request     HttpServletRequest → la requête HTTP
     * @param response    HttpServletResponse → la réponse HTTP
     * @param filterChain FilterChain utilisé pour passer la requête au filtre suivant dans la chaîne
     * @throws ServletException En cas d'erreur de servlet
     * @throws IOException      En cas d'erreur d'entrée/sortie
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String token = null;
        String username = null;

        // Récupère l'en-tête Authorization qui contient le token JWT
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Extrait le token JWT en enlevant "Bearer "
            token = authHeader.substring(7);

            // Extrait le nom d'utilisateur à partir du token JWT
            username = jwtService.extractNomUtilisateur(token);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // Charge les détails de l'utilisateur à partir du service InfoUtilisateurService
                UserDetails userDetails = infoUtilisateurService.loadUserByUsername(username);

                // Valide le token JWT avec les détails de l'utilisateur
                if (jwtService.valideToken(token, userDetails)) {
                    // Crée l'objet d'authentification avec les détails de l'utilisateur
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Configure l'authentification dans le contexte de sécurité
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        // Passe la requête au filtre suivant dans la chaîne
        filterChain.doFilter(request, response);
    }

}
