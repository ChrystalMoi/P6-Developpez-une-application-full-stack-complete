package com.openclassrooms.mddapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentaireDto {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String contenu;

    @NotNull
    private Long utilisateur_id;
    private String utilisateur_nom;

    @NotNull
    private Long article_id;

    private LocalDateTime dateCreation;
}
