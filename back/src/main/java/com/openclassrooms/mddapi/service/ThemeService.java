package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;

import java.util.List;

public interface ThemeService {

    /**
     * Récupère un thème par son identifiant
     *
     * @param id L'identifiant du thème à récupérer
     * @return Le thème correspondant à l'identifiant spécifier
     * @throws EntiteNonTrouveeException Si le thème associé à l'identifiant spécifié n'est pas trouvé
     */
    Theme getThemeParId(final Long id) throws EntiteNonTrouveeException;

    /**
     * Récupère tous les thèmes disponibles
     *
     * @return Une liste contenant tous les thèmes disponibles
     */
    List<Theme> getTousLesThemes();
}
