package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentaireDto;
import com.openclassrooms.mddapi.entity.Commentaire;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.ArticleServiceImpl;
import com.openclassrooms.mddapi.service.InfoUtilisateurService;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class CommentaireMapper implements DtoEntiteMapper<CommentaireDto, Commentaire>{

    @Autowired
    InfoUtilisateurService infoUtilisateurService = new InfoUtilisateurServiceImpl();

    @Autowired
    ArticleService articleService = new ArticleServiceImpl();

    /**
     * Mappe un CommentaireDto en une entité Commentaire
     *
     * @param commentaireDto le DTO à mapper
     * @return l'entité Commentaire correspondante
     */
    @Mappings({
            @Mapping(target = "auteur", expression = "java(infoUtilisateurService.getUtilisateurParId(commentaireDto.getAuteurId()))"),
            @Mapping(target = "article", expression = "java(articleService.getArticleParId(commentaireDto.getArticleId()))")
    })
    public abstract Commentaire mapToEntite(CommentaireDto commentaireDto);

    /**
     * Mappe une entité Commentaire en un DTO CommentaireDto
     *
     * @param commentaire l'entité à mapper
     * @return le DTO CommentaireDto correspondant
     */
    @Mappings({
            @Mapping(source = "auteur.id", target = "auteurId"),
            @Mapping(source = "auteur.nom", target = "auteurNom"),
            @Mapping(source = "article.id", target = "articleId")
    })
    public abstract CommentaireDto mapToDto(Commentaire commentaire);
}
