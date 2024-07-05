package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.service.InfoUtilisateurService;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import com.openclassrooms.mddapi.service.ThemeService;
import com.openclassrooms.mddapi.service.ThemeServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "Spring")
public abstract class ArticleMapper implements DtoEntiteMapper<ArticleDto, Article>{

    @Autowired
    InfoUtilisateurService infoUtilisateurService = new InfoUtilisateurServiceImpl();

    @Autowired
    ThemeService themeService = new ThemeServiceImpl();

    /**
     * Mappe un ArticleDto en une entité Article.
     *
     * @param articleDto le DTO à mapper
     * @return l'entité Article correspondante
     */
    @Mappings({
            @Mapping(target = "nomUtilisateur", expression = "java(infoUtilisateurService.getUtilisateurParId(articleDto.getUtilisateur_id()))"),
            @Mapping(target = "theme", expression = "java(themeService.getThemeParId(articleDto.getTheme_id()))"),
    })
    public abstract Article mapToEntite(ArticleDto articleDto);

    /**
     * Mappe une entité Article en un DTO ArticleDto
     *
     * @param article l'entité à mapper
     * @return le DTO ArticleDto correspondant
     */
    @Mappings({
            @Mapping(source = "nomUtilisateur.id", target = "utilisateur_id"),
            @Mapping(source = "nomUtilisateur.nom", target = "utilisateur_nom"),
            @Mapping(source = "theme.id", target = "theme_id"),
            @Mapping(source = "theme.nom", target = "theme_nom")
    })
    public abstract ArticleDto mapToDto(Article article);
}
