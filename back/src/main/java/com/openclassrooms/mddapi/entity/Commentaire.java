package com.openclassrooms.mddapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Commentaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le contenu du commentaire ne peut pas être vide")
    @Size(max = 500, message = "Le contenu du commentaire ne peut pas dépasser {max} caractères")
    private String contenu;

    @ManyToOne
    @JoinColumn(name="utilisateur_id", referencedColumnName = "id")
    private InfoUtilisateur auteur;

    @ManyToOne
    @JoinColumn(name="article_id", referencedColumnName = "id")
    private Article article;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateCreation;
}
