package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.Commentaire;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;

import java.util.List;

public interface CommentaireService {
    /**
     * Récupère tous les commentaires pour un article avec l'identifiant donné
     * @param id Identifiant Long pour l'article
     * @return Liste de tous les commentaires pour l'article donné
     */
    List<Commentaire> obtenirTousLesCommentairesAvecIdArticle(final Long id);

    /**
     * Tente de créer et de sauvegarder un commentaire
     * @param commentaire Commentaire à créer
     * @throws EntiteNonTrouveeException si l'identifiant de l'article n'est pas trouvé
     */
    void sauvegarderCommentaire(Commentaire commentaire) throws EntiteNonTrouveeException;
}
