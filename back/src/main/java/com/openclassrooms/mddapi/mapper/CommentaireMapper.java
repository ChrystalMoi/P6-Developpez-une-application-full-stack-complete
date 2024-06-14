package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.CommentaireDto;
import com.openclassrooms.mddapi.entity.Commentaire;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.InfoUtilisateurService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public abstract class CommentaireMapper implements DtoEntiteMapper<CommentaireDto, Commentaire>{

    @Autowired
    private InfoUtilisateurService infoUtilisateurService;

    @Autowired
    private ArticleService articleService;

    /**
     * Mappe un CommentaireDto en une entité Commentaire
     *
     * @param commentaireDto le DTO à mapper
     * @return l'entité Commentaire correspondante
     */
    @Mappings({
            @Mapping(target = "nomUtilisateur", expression = "java(infoUtilisateurService.getUserById(commentaireDto.getUtilisateur_id()))"),
            @Mapping(target = "article", expression = "java(articleService.getArticleById(commentaireDto.getArticle_id()))")
    })
    public abstract Commentaire mapToEntity(CommentaireDto commentaireDto);

    /**
     * Mappe une entité Commentaire en un DTO CommentaireDto
     *
     * @param commentaire l'entité à mapper
     * @return le DTO CommentaireDto correspondant
     */
    @Mappings({
            @Mapping(source = "nomUtilisateur.id", target = "utilisateur_id"),
            @Mapping(source = "nomUtilisateur.nom", target = "utilisateur_nom"),
            @Mapping(source = "article.id", target = "article_id")
    })
    public abstract CommentaireDto mapToDto(Commentaire commentaire);
}
