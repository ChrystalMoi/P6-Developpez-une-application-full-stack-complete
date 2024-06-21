package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.payload.AuthentificationRequest;
import com.openclassrooms.mddapi.payload.CreerUtilisateurRequest;
import com.openclassrooms.mddapi.service.InfoUtilisateurService;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import com.openclassrooms.mddapi.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final InfoUtilisateurService infoUtilisateurService = new InfoUtilisateurServiceImpl();

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /* ================================
        auth/register (POST)
    ================================*/
    /**
     * Enregistre un nouvel utilisateur et génère un token (JWT)
     *
     * @param creerUtilisateurRequest objet contenant les informations de l'utilisateur à créer
     * @return une map contenant le token JWT généré
     */
    @Operation(
            summary = "Enregistre un nouvel utilisateur",
            description = "Cette méthode permet d'enregistrer un nouvel utilisateur et de générer un token JWT",
            tags = { "Utilisateur" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur bien enregistré"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    @PostMapping("/register")
    public Map<String, String> register(@Valid @RequestBody CreerUtilisateurRequest creerUtilisateurRequest) {
        // Créer un nouvel utilisateur avec les informations fournies
        InfoUtilisateur infoUtilisateur = InfoUtilisateur.builder()
                .nom(creerUtilisateurRequest.getNom())
                .email(creerUtilisateurRequest.getEmail())
                .motDePasse(creerUtilisateurRequest.getMotDePasse())
                .roles("ROLE_USER")
                .build();

        // Sauvegarder le nouvel utilisateur
        infoUtilisateurService.creerUtilisateur(infoUtilisateur);

        // Générer un token JWT pour le nouvel utilisateur
        String token = jwtService.generateToken(infoUtilisateur.getEmail());

        // Retourner le token dans une réponse sous forme de map
        return Collections.singletonMap("token", token);
    }


    /* ================================
        auth/login (POST)
    ================================*/
    /**
     * Endpoint pour l'authentification d'un utilisateur et la génération d'un token JWT
     *
     * @param requeteAuthentification Objet {@link AuthentificationRequest} contenant l'email et le mot de passe de l'utilisateur
     * @return ResponseEntity contenant un token JWT si l'authentification est réussie
     * @throws UsernameNotFoundException Si les identifiants fournis sont invalides
     */
    @Operation(
            summary = "Authentification et génération de token JWT",
            description = "Authentifie un utilisateur avec les identifiants fournis et génère un token JWT en cas de succès",
            tags = { "Authentification" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Authentification réussie et token JWT généré"),
            @ApiResponse(responseCode = "401", description = "Identifiants invalides")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthentificationRequest requeteAuthentification) {
        // Authentification de l'utilisateur en utilisant le gestionnaire d'authentification
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requeteAuthentification.getEmail(), requeteAuthentification.getMotDePasse()));

        // Vérification que l'authentification est réussie
        if (authentication.isAuthenticated()) {
            // Génération d'un token JWT pour l'utilisateur authentifié
            String token = jwtService.generateToken(requeteAuthentification.getEmail());

            // Préparation de la réponse (avec le token JWT)
            Map<String, String> response = new HashMap<>();
            response.put("token", token);

            // Retourne une réponse HTTP 200 (OK) avec le token JWT dans le corps de la réponse
            return ResponseEntity.ok(response);
        } else {
            // Si l'authentification échoue, lancement d'une exception indiquant des identifiants invalides
            throw new UsernameNotFoundException("Identifiants invalides");
        }
    }

    /* ================================
        auth/me (GET)
    ================================*/

    /* ================================
        auth/me (PATCH)
    ================================*/

}
