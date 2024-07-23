package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.UtilisateurDto;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.exception.EmailDejaUtiliseeException;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;
import com.openclassrooms.mddapi.exception.ErreurGeneriqueException;
import com.openclassrooms.mddapi.mapper.UtilisateurMapper;
import com.openclassrooms.mddapi.payload.AuthentificationRequest;
import com.openclassrooms.mddapi.payload.CreerUtilisateurRequest;
import com.openclassrooms.mddapi.payload.ModificationUtilisateurRequest;
import com.openclassrooms.mddapi.service.InfoUtilisateurService;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import com.openclassrooms.mddapi.service.JwtService;
import io.jsonwebtoken.Jwt;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private UtilisateurMapper utilisateurMapper;

    @Autowired
    private AuthenticationManager authenticationManager;

    /* ================================
        auth/welcome (GET)
    ================================*/

    /**
     * Permet de tester si le serveur est en ligne
     * @return un texte de bienvenue
     */
    @Operation(hidden=true)
    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }

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
            @ApiResponse(responseCode = "401", description = "Identifiants invalides"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthentificationRequest requeteAuthentification) {
        try {
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
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    /* ================================
        auth/me (GET)
    ================================*/
    /**
     * Récupère le profil de l'utilisateur connecté
     *
     * @param jwt Le jeton JWT d'authentification
     * @return Un objet UtilisateurDto représentant le profil de l'utilisateur
     */
    @Operation(
            summary = "Récupère le profil de l'utilisateur connecté",
            description = "Cette méthode permet de récupérer les informations de profil de l'utilisateur connecté en utilisant le jeton JWT fourni dans l'en-tête Authorization",
            tags = { "Profil Utilisateur" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil utilisateur récupéré avec succès"),
            @ApiResponse(responseCode = "400", description = "JWT absent de la requête"),
            @ApiResponse(responseCode = "401", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("/me")
    @Secured("ROLE_USER")
    public UtilisateurDto profilUtilisateurConnecter(@RequestHeader(value="Authorization",required=false) String jwt) throws ErreurGeneriqueException {
        if (jwt==null) throw new ErreurGeneriqueException("Le JWT est absent");
        String username = jwtService.extractNomUtilisateur(jwt.substring(7));
        return utilisateurMapper.mapToDto(infoUtilisateurService.getUtilisateurParNomUtilisateur(username));
    }

    /* ================================
        auth/me (PUT)
    ================================*/
    /**
     * Modifie le profil de l'utilisateur connecté
     *
     * @param jwt Le jeton JWT d'authentification
     * @param modificationUtilisateurRequest Les détails de la modification du profil utilisateur
     * @return Un map contenant un token JWT mis à jour en cas de succès
     * @throws EntiteNonTrouveeException Si l'utilisateur n'est pas trouvé
     * @throws EmailDejaUtiliseeException Si l'adresse e-mail est déjà utilisée par un autre utilisateur
     * @throws ConstraintViolationException Si une violation de contrainte se produit lors de la validation
     */
    @Operation(
            summary = "Modifie le profil de l'utilisateur connecté",
            description = "Cette méthode permet à un utilisateur connecté de modifier son nom, son adresse e-mail ou son mot de passe",
            tags = { "Profil Utilisateur" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Profil utilisateur modifié avec succès"),
            @ApiResponse(responseCode = "400", description = "Requête invalide"),
            @ApiResponse(responseCode = "401", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PutMapping("/me")
    @Secured("ROLE_USER")
    public Map<String, String> modificationProfilUtilisateur(
            @RequestHeader(value = "Authorization", required = false) String jwt,
            @Valid @RequestBody ModificationUtilisateurRequest modificationUtilisateurRequest
    ) throws EntiteNonTrouveeException, EmailDejaUtiliseeException, ConstraintViolationException {
        // Extraire le nom d'utilisateur du token JWT
        String username = jwtService.extractNomUtilisateur(jwt.substring(7));

        // Récupérer l'utilisateur à partir du service avec le nom d'utilisateur
        InfoUtilisateur utilisateur = infoUtilisateurService.getUtilisateurParNomUtilisateur(username);

        // Mettre à jour les informations de l'utilisateur si elles sont fournies
        if (modificationUtilisateurRequest.getNom() != null && !modificationUtilisateurRequest.getNom().isEmpty()) {
            utilisateur.setNom(modificationUtilisateurRequest.getNom());
        }
        if (modificationUtilisateurRequest.getEmail() != null && !modificationUtilisateurRequest.getEmail().isEmpty()) {
            utilisateur.setEmail(modificationUtilisateurRequest.getEmail());
        }
        if (modificationUtilisateurRequest.getMotDePasse() != null && !modificationUtilisateurRequest.getMotDePasse().isEmpty()) {
            utilisateur.setMotDePasse(modificationUtilisateurRequest.getMotDePasse());
        }

        // Appeler le service pour modifier l'utilisateur
        infoUtilisateurService.modifierUtilisateur(utilisateur, modificationUtilisateurRequest.getMotDePasse() != null && !modificationUtilisateurRequest.getMotDePasse().isEmpty());

        // Générer un nouveau token JWT pour l'utilisateur mis à jour
        Map<String, String> map = new HashMap<>();
        map.put("token", jwtService.generateToken(utilisateur.getEmail()));

        // Retourner le token dans une réponse sous forme de map
        return map;
    }
}
