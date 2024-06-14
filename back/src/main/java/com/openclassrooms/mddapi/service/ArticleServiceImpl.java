package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.exception.ArticleNotFoundException;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du service pour la gestion des articles
 */
@Service
@RequiredArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    /**
     * Récupère un article par son id
     * @param articleId L'id de l'article à récupérer
     * @return L'article correspondant à l'id spécifié
     * @throws ArticleNotFoundException si aucun article n'est trouvé avec l'id spécifié
     */
    @Override
    public Article getArticleById(final Long articleId) throws ArticleNotFoundException {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(Article.class, "id", articleId.toString()));
    }

    /**
     * Enregistre un nouvel article
     * @param article L'article à enregistrer
     * @throws ArticleNotFoundException si l'article n'a pas pu être enregistré
     */
    @Override
    public void saveArticle(final Article article) throws ArticleNotFoundException {
        articleRepository.save(article);
    }

    /**
     * Récupère tous les articles associés à un thème spécifié, triés par date de création dé-croissante
     * @param id L'id du thème
     * @return La liste des articles associés au thème spécifié
     */
    @Override
    public List<Article> getAllArticlesWithThemeId(final Long id) {
        return articleRepository.findByThemeIdOrderByCreeADesc(id);
    }

    /**
     * Récupère tous les articles associés à une liste de thèmes spécifiée, triés par date de création dé-croissante
     * @param ids La liste des id de thèmes
     * @return La liste des articles associés aux thèmes spécifiés
     */
    @Override
    public List<Article> getAllArticlesInThemeIds(final List<Long> ids) {
        return articleRepository.findByThemeIdInOrderByCreeADesc(ids);
    }
}
