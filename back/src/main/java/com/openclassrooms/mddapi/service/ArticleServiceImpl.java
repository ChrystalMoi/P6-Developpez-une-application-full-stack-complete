package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.exception.ArticleNotFoundException;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du service pour la gestion des articles
 */
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleMapper articleMapper;

    /**
     * Récupère un article par son id
     * @param articleId L'id de l'article à récupérer
     * @return L'article correspondant à l'id spécifié
     * @throws ArticleNotFoundException si aucun article n'est trouvé avec l'id spécifié
     */
    @Override
    public Article getArticleParId(final Long articleId) throws ArticleNotFoundException {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new ArticleNotFoundException(Article.class, "id", articleId.toString()));
    }

    /**
     * Enregistre un nouvel article
     * @param article L'article à enregistrer
     * @throws ArticleNotFoundException si l'article n'a pas pu être enregistré
     */
    @Override
    public void enregistreArticle(final Article article) throws ArticleNotFoundException {
        articleRepository.save(article);
    }

    /**
     * Récupère tous les articles associés à un thème spécifié, triés par date de création dé-croissante
     * @param id L'id du thème
     * @return La liste des articles associés au thème spécifié
     */
    @Override
    public List<Article> getTousLesArticlesAvecThemeId(final Long id) {
        return articleRepository.findByThemeIdOrderByDateCreationDesc(id);
    }

    /**
     * Récupère tous les articles associés à une liste de thèmes spécifiée, triés par date de création dé-croissante
     * @param ids La liste des id de thèmes
     * @return La liste des articles associés aux thèmes spécifiés
     */
    @Override
    public List<Article> getTousLesArticlesDansLesThemeIds(final List<Long> ids) {
        return articleRepository.findByThemeIdInOrderByDateCreationDesc(ids);
    }

    /**
     * Récupère tous les articles
     * @return la liste de tous les articles
     */
    @Override
    public List<ArticleDto> getAllArticles() {
        // Récupérer dans le ArticleRepository la liste des ArticleEntity
        List<Article> listeArticleEntity = articleRepository.findAll();

        // Traduire les ArticleEntitys en ArticleDTO pour éliminer les informations inutiles
        List<ArticleDto> listeArticleDto = articleMapper.mapToDto(listeArticleEntity);

        // Retourner la liste au format DTO (format attendu)
        return listeArticleDto;
    }
}
