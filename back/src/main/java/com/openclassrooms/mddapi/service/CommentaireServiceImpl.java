package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.Commentaire;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;
import com.openclassrooms.mddapi.repository.CommentaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du service pour gérer les opérations relatives aux commentaires
 */
@Service
@RequiredArgsConstructor
public class CommentaireServiceImpl implements CommentaireService {
    @Autowired
    private CommentaireRepository commentaireRepository;

    /**
     * Obtient tous les commentaires associés à un article donné, triés par date de création décroissante
     *
     * @param id L'identifiant de l'article
     * @return Une liste de commentaires pour l'article spécifié
     */
    @Override
    public List<Commentaire> obtenirTousLesCommentairesAvecIdArticle(final Long id) {
        return commentaireRepository.findCommentairesByArticleIdOrderByDateCreationDesc(id);
    }

    /**
     * Sauvegarde un commentaire
     *
     * @param commentaire Le commentaire à sauvegarder
     * @throws EntiteNonTrouveeException Si l'entité associée au commentaire n'est pas trouvée.
     */
    @Override
    public void sauvegarderCommentaire(Commentaire commentaire) throws EntiteNonTrouveeException {
        commentaireRepository.save(commentaire);
    }
}
