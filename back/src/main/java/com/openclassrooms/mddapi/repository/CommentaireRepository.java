package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Commentaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentaireRepository extends JpaRepository<Commentaire, Long> {
    /**
     * Trouve les commentaires associés à un article spécifique.
     * @param articleId l'ID de l'article
     * @return une liste de commentaires
     */
    List<Commentaire> findByArticleId(Long articleId);

    /**
     * Trouve les commentaires associés à un article spécifique et les trie par date de création décroissante.
     * @param articleId l'ID de l'article
     * @return une liste de commentaires triés par date de création décroissante
     */
    List<Commentaire> findCommentairesByArticleIdOrderByCreeADesc(Long articleId);
}
