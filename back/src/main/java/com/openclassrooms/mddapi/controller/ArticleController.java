package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.dto.CommentaireDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Commentaire;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.mapper.CommentaireMapper;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ArticleServiceImpl;
import com.openclassrooms.mddapi.service.CommentaireService;
import com.openclassrooms.mddapi.service.CommentaireServiceImpl;
import com.openclassrooms.mddapi.service.InfoUtilisateurService;
import com.openclassrooms.mddapi.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private final ArticleService articleService= new ArticleServiceImpl();

    @Autowired
    private final CommentaireService commentaireService= new CommentaireServiceImpl();

    @Autowired
    private InfoUtilisateurService infoUtilisateurService;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CommentaireMapper commentaireMapper;

    /* ================================
        article/{id} (GET)
    ================================*/
    /**
     * Récupère un article par son identifiant
     *
     * @param id l'identifiant de l'article
     * @return l'ArticleDto correspondant
     * @throws EntiteNonTrouveeException si l'article n'est pas trouvé
     */
    @Operation(
            summary = "Affiche l'article requis",
            description = "Cette méthode permet de récupérer les détails d'un article en fournissant son identifiant",
            tags = { "Article" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Article bien affiché"),
            @ApiResponse(responseCode = "400", description = "L'id n'existe pas"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    @GetMapping("/{id}")
    @Secured("ROLE_USER") // Seuls les utilisateurs authentifiés peuvent accéder à /article/{id}
    public ArticleDto getArticleParId(@PathVariable("id") final long id) throws EntiteNonTrouveeException {
        Article article = articleService.getArticleParId(id);
        return articleMapper.mapToDto(article);
    }

    /* ================================
        article/{id}/commentaire (POST)
    ================================*/
    /**
     * Ajoute un commentaire à un article spécifié par son identifiant
     *
     * @param id l'identifiant de l'article
     * @param requestBody une map contenant le contenu du commentaire
     * @param jwt le jeton d'autorisation contenant les informations de l'utilisateur
     * @return une map indiquant le succès de l'opération
     * @throws EntiteNonTrouveeException si l'utilisateur correspondant au nom d'utilisateur dans le JWT n'est pas trouvé
     */
    @Operation(
            summary = "Ajoute un commentaire à l'article",
            description = "Cette méthode permet d'ajouter un commentaire à un article existant en fournissant l'identifiant de l'article et les détails du commentaire",
            tags = { "Commentaire" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commentaire ajouté avec succès"),
            @ApiResponse(responseCode = "400", description = "Article ou commentaire invalide"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé"),
            @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
    })
    @PostMapping("/{id}/commentaire")
    @Secured("ROLE_USER")
    public Map<String, String> ajouterCommentaire( @PathVariable("id") long id,
                                                   @RequestBody Map<String, String> requestBody,
                                                   @RequestHeader(value = "Authorization", required = false) String jwt) throws EntiteNonTrouveeException {
        // Extraire le nom d'utilisateur du jeton JWT
        String nomUtilisateur = jwtService.extractNomUtilisateur(jwt.substring(7));
        InfoUtilisateur utilisateur = infoUtilisateurService.getUtilisateurParNomUtilisateur(nomUtilisateur);

        if (utilisateur == null) {
            throw new EntiteNonTrouveeException(InfoUtilisateur.class, "nom", nomUtilisateur);
        }

        // Créer un objet CommentaireDto avec le contenu et l'id article
        CommentaireDto commentaireDto = new CommentaireDto();
        commentaireDto.setArticleId(id);
        commentaireDto.setContenu(requestBody.get("contenu"));
        commentaireDto.setAuteurId(utilisateur.getId());
        commentaireDto.setAuteurNom(utilisateur.getNom());

        // Transformer le DTO vers une entité de commentaire via le mapper
        Commentaire commentaire = commentaireMapper.mapToEntite(commentaireDto);

        // Appeler le service pour sauvegarder le commentaire
        commentaireService.sauvegarderCommentaire(commentaire);

        // Retourner une réponse sous forme de map
        Map<String, String> response = new HashMap<>();
        response.put("response", "Commentaire créé avec succès");
        return response;
    }

    /* ================================
        article/{id}/commentaire (GET)
    ================================*/
    /**
     * Récupère tous les commentaires associés à un article avec l'identifiant donné
     *
     * @param id l'identifiant de l'article
     * @return la liste des CommentaireDto associés à l'article donné
     * @throws EntiteNonTrouveeException si l'article n'est pas trouvé
     */
    @Operation(
            summary = "Récupère tous les commentaires associés à un article",
            description = "Cette méthode permet de récupérer tous les commentaires associés à un article en fournissant l'identifiant de l'article",
            tags = { "Commentaire" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des commentaires récupérée avec succès"),
            @ApiResponse(responseCode = "400", description = "L'id de l'article est invalide"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    @GetMapping("/{id}/commentaire")
    public List<CommentaireDto> obtenirCommentairesPourArticle(@PathVariable("id") final long id) throws EntiteNonTrouveeException {
        List<Commentaire> commentaires = commentaireService.obtenirTousLesCommentairesAvecIdArticle(id);
        return commentaireMapper.mapToDto(commentaires);
    }

    /* ================================
            article (GET)
        ================================*/
    @Operation(
            summary = "Affiche les articles",
            description = "Cette méthode permet de récupérer tous les articles",
            tags = { "Article" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Articles bien affiché"),
            @ApiResponse(responseCode = "403", description = "Accès non autorisé")
    })
    @GetMapping("")
    @Secured("ROLE_USER")
    public List<ArticleDto> getAllArticles(){
        return articleService.getAllArticles();
    }

}
