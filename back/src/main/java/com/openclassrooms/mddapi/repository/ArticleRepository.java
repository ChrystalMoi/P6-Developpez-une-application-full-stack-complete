package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    /**
     * Trouve les articles associés à un thème spécifique
     * @param themeId l'ID du thème
     * @return une liste d'articles
     */
    List<Article> findByThemeId(Long themeId);

    /**
     * Trouve les articles créés par un utilisateur spécifique
     * @param utilisateurId l'ID de l'utilisateur
     * @return une liste d'articles
     */
    List<Article> findByAuteurId(Long utilisateurId);

    /**
     * Trouve les articles associés à un thème spécifique et les trie par date de création dé-croissante
     * @param themeId l'ID du thème
     * @return une liste d'articles triés par date de création dé-croissante
     */
    List<Article> findByThemeIdOrderByDateCreationDesc(Long themeId);

    /**
     * Trouve les articles associés à une liste de thèmes et les trie par date de création dé-croissante
     * @param themeIds la liste des IDs de thèmes
     * @return une liste d'articles triés par date de création dé-croissante
     */
    List<Article> findByThemeIdInOrderByDateCreationDesc(List<Long> themeIds);
}
