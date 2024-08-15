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
public class CommentaireDto {

    private Long id;

    @NotNull
    @Size(max = 500)
    private String contenu;

    @NotNull
    private Long auteurId;
    private String auteurNom;

    @NotNull
    private Long articleId;

    private LocalDateTime dateCreation;
}
