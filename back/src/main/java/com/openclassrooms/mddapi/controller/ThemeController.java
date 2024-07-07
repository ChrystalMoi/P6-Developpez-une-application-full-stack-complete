package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ArticleServiceImpl;
import com.openclassrooms.mddapi.service.InfoUtilisateurService;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import com.openclassrooms.mddapi.service.JwtService;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.ThemeServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/theme")
public class ThemeController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private final ThemeService themeService = new ThemeServiceImpl();

    @Autowired
    private final ArticleService articleService= new ArticleServiceImpl();

    @Autowired
    private final InfoUtilisateurService infoUtilisateurService = new InfoUtilisateurServiceImpl();

    /* ================================
        /theme (GET)
    ================================*/
    /**
     * Récupère tous les thèmes disponibles dans une liste
     *
     * @return Une liste de tous les thèmes disponibles
     */
    @Operation(
            summary = "Récupère tous les thèmes disponibles",
            description = "Cette méthode permet à un utilisateur connecté de récupérer tous les thèmes disponibles",
            tags = { "Thèmes" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des thèmes récupérée avec succès"),
            @ApiResponse(responseCode = "401", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("")
    @Secured("ROLE_USER")
    public List<Theme> getTousLesThemes() {
        return themeService.getTousLesThemes();
    }

    /* ================================
        /theme (POST)
    ================================*/
    /**
     * Crée un nouveau thème
     *
     * @param theme Les détails du thème à créer
     * @return Un message indiquant que le thème a été créé avec succès
     */
    @Operation(
            summary = "Crée un nouveau thème",
            description = "Permet à un utilisateur administrateur de créer un nouveau thème",
            tags = { "Thèmes" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thème créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Détails du thème invalides"),
            @ApiResponse(responseCode = "401", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping("")
    public Map<String, String> creerTheme(
            @Parameter(description = "Détails du thème à créer", required = true)
            @RequestBody Theme theme
    ) {
        // Enregistrement du thème dans la base de données
        themeService.enregistrerTheme(theme);

        // Création de la réponse avec un message de confirmation
        Map<String, String> map = new HashMap<>();
        map.put("response", "Thème créé avec succès");
        return map;
    }

    /* ================================
        /theme/{id} (GET)
    ================================*/
    /**
     * Récupère un thème par son identifiant
     *
     * @param id L'identifiant du thème à récupérer
     * @return Le thème correspondant à l'identifiant spécifier
     * @throws EntiteNonTrouveeException Si aucun thème correspondant à l'identifiant n'est trouvé
     */
    @Operation(
            summary = "Récupère un thème par son identifiant",
            description = "Permet à un utilisateur connecté de récupérer un thème spécifique par son identifiant",
            tags = { "Thèmes" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Thème récupéré avec succès"),
            @ApiResponse(responseCode = "404", description = "Thème non trouvé"),
            @ApiResponse(responseCode = "401", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public Theme getThemeParId(@PathVariable("id") final long id) throws EntiteNonTrouveeException {
        return themeService.getThemeParId(id);
    }

    /* ================================
        /theme/{id}/articles (POST)
    ================================*/
    /**
     * Crée un nouvel article pour un thème spécifié
     *
     * @param id L'identifiant du thème pour lequel l'article est créé
     * @param jwt Le token JWT contenant les informations d'authentification de l'utilisateur
     * @param articleDto Les détails de l'article à créer
     * @return Un message indiquant que l'article a été créé avec succès
     * @throws EntiteNonTrouveeException Si le thème ou l'utilisateur n'est pas trouvé
     */
    @Operation(
            summary = "Crée un nouvel article pour un thème spécifié",
            description = "Permet à un utilisateur connecté de créer un nouvel article pour un thème spécifié",
            tags = { "Articles", "Thèmes" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Article créé avec succès"),
            @ApiResponse(responseCode = "404", description = "Thème ou utilisateur non trouvé"),
            @ApiResponse(responseCode = "401", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping("/{id}/articles")
    @Secured("ROLE_USER")
    public Map<String, String> creationArticle(
            @Parameter(description = "L'identifiant du thème pour lequel l'article est créé", required = true)
            @PathVariable("id") final long id,

            @Parameter(description = "Token JWT pour l'authentification", required = true)
            @RequestHeader(value = "Authorization", required = false) String jwt,

            @Parameter(description = "Détails de l'article à créer", required = true)
            @RequestBody ArticleDto articleDto
    ) throws EntiteNonTrouveeException {
        // Extraction du nom d'utilisateur à partir du token JWT
        String nomUtilisateur = jwtService.extractNomUtilisateur(jwt.substring(7));
        System.out.println("Nom d'utilisateur extrait du JWT: " + nomUtilisateur);

        // Récupération de l'utilisateur à partir du nom d'utilisateur
        InfoUtilisateur utilisateur = infoUtilisateurService.getUtilisateurParNomUtilisateur(nomUtilisateur);
        System.out.println("Utilisateur récupéré: " + utilisateur);

        // Construction de l'objet Article à partir des données reçues
        Article article = Article.builder()
                .contenu(articleDto.getContenu())
                .titre(articleDto.getTitre())
                .nomUtilisateur(utilisateur)
                .theme(themeService.getThemeParId(id))
                .build();
        System.out.println("Article construit: " + article);

        // Enregistrement de l'article dans la base de données
        articleService.enregistreArticle(article);
        System.out.println("Article enregistré: " + article);

        // Création de la réponse avec un message de confirmation
        Map<String, String> map = new HashMap<>();
        map.put("response", "Article créé avec succès");
        System.out.println("Réponse: " + map);

        return map;
    }

    /* ================================
        /theme/{id}/subscription (POST)
    ================================*/
    /**
     * Inscrit l'utilisateur connecté à un thème spécifié
     *
     * @param id L'identifiant du thème auquel l'utilisateur s'abonne
     * @param jwt Le token JWT contenant les informations d'authentification de l'utilisateur
     * @return Un message indiquant le succès de l'inscription au thème ou si le thème est déjà abonné
     * @throws EntiteNonTrouveeException Si le thème ou l'utilisateur n'est pas trouvé
     */
    @Operation(
            summary = "Inscrit l'utilisateur connecté à un thème spécifié",
            description = "Permet à un utilisateur connecté de s'abonner à un thème spécifié",
            tags = { "Abonnements" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "L'abonnement au thème a été réalisé avec succès"),
            @ApiResponse(responseCode = "404", description = "Thème ou utilisateur non trouvé"),
            @ApiResponse(responseCode = "401", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @PostMapping("/{id}/subscription")
    @Secured("ROLE_USER")
    public String inscriptionTheme(
            @Parameter(description = "L'identifiant du thème auquel l'utilisateur s'abonne", required = true)
            @PathVariable("id") final long id,

            @Parameter(description = "Token JWT pour l'authentification", required = true)
            @RequestHeader(value="Authorization", required=false) String jwt
    ) throws EntiteNonTrouveeException {
        // Extraction du nom d'utilisateur à partir du token JWT
        String nomUtilisateur = jwtService.extractNomUtilisateur(jwt.substring(7));

        // Récupération de l'utilisateur à partir du service
        InfoUtilisateur utilisateur = infoUtilisateurService.getUtilisateurParNomUtilisateur(nomUtilisateur);

        // Récupération des abonnements actuel de l'utilisateur
        Set<Theme> userSubscriptions = utilisateur.getSubscriptions();

        // Récupération du thème à partir de son identifiant
        Theme theme = themeService.getThemeParId(id);

        // Ajout du thème aux abonnements de l'utilisateur et vérification si l'ajout a été effectué
        boolean aEteAbonne = userSubscriptions.add(theme);

        // Si l'abonnement a été ajouté avec succès
        if (aEteAbonne) {
            // Mise à jour des abonnements de l'utilisateur dans la base de données
            infoUtilisateurService.modifierUtilisateur(utilisateur, false);
            return "L'abonnement au thème a été réalisé avec succès";
        } else {
            // Si le thème était déjà abonné
            return "Le thème est déjà abonné";
        }
    }

    /* ================================
        /theme/{id}/subscription (DELETE)
    ================================*/
    /**
     * Endpoint pour désabonner l'utilisateur connecté d'un thème spécifié
     *
     * @param id L'identifiant du thème à désabonner
     * @param jwt Le token JWT contenant les informations d'authentification de l'utilisateur
     * @return Un message indiquant le résultat de l'opération de désabonnement
     * @throws EntiteNonTrouveeException Si le thème ou l'utilisateur n'est pas trouvé
     */
    @Operation(
            summary = "Désabonne l'utilisateur d'un thème",
            description = "Désabonne l'utilisateur connecté d'un thème spécifié par son identifiant",
            tags = { "Abonnements" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Désabonnement réussi"),
            @ApiResponse(responseCode = "404", description = "Thème ou utilisateur non trouvé"),
            @ApiResponse(responseCode = "401", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    @DeleteMapping("/{id}/subscription")
    @Secured("ROLE_USER")
    public String desabonnementTheme(
            @Parameter(description = "L'identifiant du thème à désabonner", required = true)
            @PathVariable("id") final long id,

            @Parameter(description = "Token JWT pour l'authentification", required = true)
            @RequestHeader(value="Authorization", required=false) String jwt
    ) throws EntiteNonTrouveeException {
        // Extraction du nom d'utilisateur du token JWT
        String nomUtilisateur = jwtService.extractNomUtilisateur(jwt.substring(7));

        // Récupération de l'utilisateur à partir du service
        InfoUtilisateur utilisateur = infoUtilisateurService.getUtilisateurParNomUtilisateur(nomUtilisateur);

        // Récupération des abonnements actuel de l'utilisateur
        Set<Theme> userSubscriptions = utilisateur.getSubscriptions();

        // Tentative de suppression du thème de la liste des abonnements de l'utilisateur
        if (userSubscriptions.remove(themeService.getThemeParId(id))) {
            utilisateur.setSubscriptions(userSubscriptions);

            // Mise à jour de l'utilisateur dans la base de données
            infoUtilisateurService.modifierUtilisateur(utilisateur, false);

            // Retour d'un message indiquant que le désabonnement a réussi
            return "Désabonnement réussi";
        } else {
            // Retour d'un message indiquant que le thème n'était pas abonné précédemment
            return "Le thème n'était pas abonné";
        }
    }

}
