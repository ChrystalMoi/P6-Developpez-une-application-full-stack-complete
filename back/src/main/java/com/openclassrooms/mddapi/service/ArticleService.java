package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.exception.ArticleNotFoundException;

import java.util.List;

/**
 * Service pour la gestion des articles
 */
public interface ArticleService {
    /**
     * Récupère un article par son id
     * @param articleId L'id de l'article à récupérer
     * @return L'article correspondant à l'id spécifié
     * @throws ArticleNotFoundException si aucun article n'est trouvé avec l'id spécifié
     */
    Article getArticleParId(final Long articleId) throws ArticleNotFoundException;

    /**
     * Enregistre un nouvel article
     * @param article L'article à enregistrer
     * @throws ArticleNotFoundException si l'article n'a pas pu être enregistré
     */
    void enregistreArticle(Article article) throws ArticleNotFoundException;

    /**
     * Récupère tous les articles associés à un thème spécifié
     * @param id L'id du thème
     * @return La liste des articles associés au thème spécifié
     */
    List<Article> getTousLesArticlesAvecThemeId(final Long id);

    /**
     * Récupère tous les articles associés à une liste de thèmes spécifiée
     * @param ids La liste des id de thèmes
     * @return La liste des articles associés aux thèmes spécifiés
     */
    List<Article> getTousLesArticlesDansLesThemeIds(final List<Long> ids);

    List<ArticleDto> getAllArticles();

}



