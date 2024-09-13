package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.dto.UtilisateurDto;
import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.mapper.UtilisateurMapper;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ArticleServiceImpl;
import com.openclassrooms.mddapi.service.InfoUtilisateurService;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("/utilisateur")
public class UtilisateurController {

    @Autowired
    private final InfoUtilisateurService infoUtilisateurService = new InfoUtilisateurServiceImpl();

    @Autowired
    private final ArticleService articleService = new ArticleServiceImpl();

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    @Autowired
    private ArticleMapper articleMapper;

    /* ================================
        /utilisateur/{id} (GET)
    ================================*/
    /**
     * Récupère les informations d'un utilisateur par son identifiant
     *
     * @param id l'identifiant de l'utilisateur
     * @return l'UtilisateurDto correspondant
     * @throws EntiteNonTrouveeException si l'utilisateur n'est pas trouvé
     */
    @Operation(
            summary = "Récupère les informations de l'utilisateur",
            description = "Cette méthode permet de récupérer les informations d'un utilisateur en fournissant son identifiant",
            tags = { "Utilisateur" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Utilisateur bien récupéré"),
            @ApiResponse(responseCode = "400", description = "Identifiant invalide"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public UtilisateurDto getUtilisateurParId(@PathVariable("id") final long id) throws EntiteNonTrouveeException {
        return utilisateurMapper.mapToDto(infoUtilisateurService.getUtilisateurParId(id));
    }

    /* ================================
        /utilisateur/{id}/articles (GET)
    ================================*/
    /**
     * Récupère tous les articles pour lesquels l'utilisateur est abonné aux thèmes
     *
     * @param id l'identifiant de l'utilisateur
     * @return la liste des ArticleDto correspondant aux thèmes abonnés par l'utilisateur
     * @throws EntiteNonTrouveeException si l'utilisateur ou les articles ne sont pas trouvés
     */
    @Operation(
            summary = "Récupère les articles des thèmes abonné par l'utilisateur",
            description = "Cette méthode permet de récupérer tous les articles correspondant aux thèmes auxquels l'utilisateur est abonné",
            tags = { "Article" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Articles bien récupérés"),
            @ApiResponse(responseCode = "400", description = "Id utilisateur ou articles invalides"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    @GetMapping("/{id}/articles")
    @Secured("ROLE_USER")
    public List<ArticleDto> getArticlesForUserById(@PathVariable("id") final long id) throws EntiteNonTrouveeException {
        List<Long> ids = infoUtilisateurService.getUtilisateurParId(id).getSubscriptions().stream()
                .map(Theme::getId)
                .toList();
        return articleMapper.mapToDto(articleService.getTousLesArticlesDansLesThemeIds(ids));
    }

}
