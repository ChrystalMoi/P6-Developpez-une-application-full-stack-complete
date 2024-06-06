package com.openclassrooms.mddapi.exception;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.HashMap;
import java.util.stream.IntStream;

public class EntiteNonTrouveeException extends RuntimeException {
    /**
     * Constructeur de l'exception
     *
     * @param classNonTrouvee La classe de l'entité qui n'a pas été trouvée
     * @param searchParamsMap Les paramètres de recherche utilisés pour trouver l'entitée
     */
    public EntiteNonTrouveeException(Class classNonTrouvee, String... searchParamsMap) {
        super(EntiteNonTrouveeException.genererMessage(classNonTrouvee.getSimpleName(),
                convertirEnMap(String.class, String.class, searchParamsMap)));
    }

    /**
     * Génère un message d'erreur détaillé
     *
     * @param entite Le nom de l'entité qui n'a pas été trouvée
     * @param parametresRecherche Les paramètres de recherche utilisés
     * @return Un message d'erreur détaillé
     */
    private static String genererMessage(String entite, Map<String, String> parametresRecherche) {
        return StringUtils.capitalize(entite) +
                " n'a pas été trouvée pour les paramètres " +
                parametresRecherche;
    }

    /**
     * Convertit une liste de paramètres de recherche en une map
     *
     * @param <C> Le type des clés de la map
     * @param <V> Le type des valeurs de la map
     * @param typeCle Le type des clés
     * @param typeValeur Le type des valeurs
     * @param entrees Les paramètres de recherche sous forme de paires clé-valeur
     * @return Une map contenant les paramètres de recherche
     * @throws IllegalArgumentException Si le nombre d'entrées est impair
     */
    private static <C, V> Map <C, V> convertirEnMap(
            Class<C> typeCle, Class<V> typeValeur, Object... entrees) {
        if (entrees.length % 2 == 1)
            throw new IllegalArgumentException("Entrées invalides");
        return IntStream.range(0, entrees.length / 2).map(i -> i * 2)
                .collect(HashMap::new,
                        (m, i) -> m.put(typeCle.cast(entrees[i]), typeValeur.cast(entrees[i + 1])),
                        Map::putAll);
    }
}
