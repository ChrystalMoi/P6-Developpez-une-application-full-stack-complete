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
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping("")
    @Secured("ROLE_USER")
    public List<Theme> getTousLesThemes() {
        return themeService.getTousLesThemes();
    }

    /* ================================
        /theme/{id} (GET)
    ================================*/
    @GetMapping("/{id}")
    @Secured("ROLE_USER")
    public Theme getThemeParId(@PathVariable("id") final long id) throws EntiteNonTrouveeException {
        return themeService.getThemeParId(id);
    }

    /* ================================
        /theme/{id}/articles (POST)
    ================================*/
    @PostMapping("/{id}/articles")
    @Secured("ROLE_USER")
    public Map<String,String> creationArticle(@PathVariable("id") final long id, @RequestHeader(value="Authorization",required=false) String jwt, @RequestBody ArticleDto articleDto) throws EntiteNonTrouveeException {
        String nomUtilisateur = jwtService.extractNomUtilisateur(jwt.substring(7));

        Article article = Article.builder()
                .contenu(articleDto.getContenu())
                .titre(articleDto.getTitre())
                .nomUtilisateur(infoUtilisateurService.getUtilisateurParNomUtilisateur(nomUtilisateur))
                .theme(themeService.getThemeParId(id))
                .build();
        articleService.enregistreArticle(article);

        Map<String,String> map = new HashMap<>();
        map.put("response","Article créé avec succès");

        return map;
    }

    /* ================================
        /theme/{id}/subscription (POST)
    ================================*/
    @PostMapping("/{id}/subscription")
    @Secured("ROLE_USER")
    public String inscriptionTheme(@PathVariable("id") final long id, @RequestHeader(value="Authorization",required=false) String jwt) throws EntiteNonTrouveeException {
        String nomUtilisateur = jwtService.extractNomUtilisateur(jwt.substring(7));
        InfoUtilisateur utilisateur = infoUtilisateurService.getUtilisateurParNomUtilisateur(nomUtilisateur);
        Set<Theme> userSubscriptions = utilisateur.getSubscriptions();

        if (userSubscriptions.add(themeService.getThemeParId(id))) {
            utilisateur.setSubscriptions(userSubscriptions);
            infoUtilisateurService.modifierUtilisateur(utilisateur,false);
            return "L'abonnement au thème a été réalisé avec succès";
        } else {
            return "Le thème est déjà abonné";
        }
    }

    /* ================================
        /theme/{id}/subscription (DELETE)
    ================================*/
    @DeleteMapping("/{id}/subscription")
    @Secured("ROLE_USER")
    public String desabonnementTheme(@PathVariable("id") final long id, @RequestHeader(value="Authorization",required=false) String jwt) throws EntiteNonTrouveeException {
        String nomUtilisateur = jwtService.extractNomUtilisateur(jwt.substring(7));
        InfoUtilisateur utilisateur = infoUtilisateurService.getUtilisateurParNomUtilisateur(nomUtilisateur);
        Set<Theme> userSubscriptions = utilisateur.getSubscriptions();

        if (userSubscriptions.remove(themeService.getThemeParId(id))) {
            utilisateur.setSubscriptions(userSubscriptions);
            infoUtilisateurService.modifierUtilisateur(utilisateur,false);
            return "Le désabonnement à été effectué avec succès";
        } else {
            return "Le theme n'a pas été enregistré pour l'abonnement";
        }
    }
}
