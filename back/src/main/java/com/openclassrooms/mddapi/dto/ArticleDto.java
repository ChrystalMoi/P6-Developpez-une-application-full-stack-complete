package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDto {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String titre;

    @NotNull
    @Size(max = 100)
    private String contenu;

    private Long utilisateur_id;
    private String utilisateur_nom;

    private Long theme_id;
    private String theme_nom;

    private LocalDateTime dateCreation;
}
