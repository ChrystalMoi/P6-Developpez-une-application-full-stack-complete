package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implémentation du service pour gérer les opérations relatives aux thèmes
 */
@Service
public class ThemeServiceImpl implements ThemeService {
    @Autowired
    private ThemeRepository themeRepository;

    /**
     * Récupère un thème par son identifiant
     *
     * @param id L'identifiant du thème à récupérer
     * @return Le thème correspondant à l'identifiant spécifié
     * @throws EntiteNonTrouveeException Si le thème associé à l'identifiant spécifié n'est pas trouvé
     */
    @Override
    public Theme getThemeById(final Long id) throws EntiteNonTrouveeException {
        return themeRepository.findById(id)
                .orElseThrow(() -> new EntiteNonTrouveeException(Theme.class, "id", id.toString()));
    }

    /**
     * Récupère tous les thèmes disponibles
     *
     * @return Une liste contenant tous les thèmes disponibles
     */
    @Override
    public List<Theme> getAllThemes() {
        return themeRepository.findAll();
    }
}
