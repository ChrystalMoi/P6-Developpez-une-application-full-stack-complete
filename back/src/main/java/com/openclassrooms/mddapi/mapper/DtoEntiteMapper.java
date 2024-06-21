package com.openclassrooms.mddapi.mapper;

import java.util.List;

/**
 * Interface pour mapper entre des DTOs et des Entités
 *
 * @param <Dto>   le type de l'objet de transfert de données (Data Transfert Object)
 * @param <Entite> le type de l'entité
 */
public interface DtoEntiteMapper<Dto, Entite> {

    /**
     * Convertit un DTO en une entité
     *
     * @param dto l'objet de transfert de données à convertir
     * @return l'entité correspondante
     */
    Entite toEntity(Dto dto);

    /**
     * Convertit une entité en un DTO
     *
     * @param entity l'entité à convertir
     * @return l'objet de transfert de données correspondant
     */
    Dto toDto(Entite entity);

    /**
     * Convertit une liste de DTOs en une liste d'entités
     *
     * @param dtoList la liste d'objets de transfert de données à convertir
     * @return la liste correspondante d'entités
     */
    List<Entite> toEntity(List<Dto> dtoList);

    /**
     * Convertit une liste d'entités en une liste de DTOs
     *
     * @param entityList la liste d'entités à convertir
     * @return la liste correspondante d'objets de transfert de données
     */
    List<Dto> toDto(List<Entite> entityList);
}