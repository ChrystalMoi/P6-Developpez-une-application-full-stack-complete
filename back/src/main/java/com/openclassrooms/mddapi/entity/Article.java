package com.openclassrooms.mddapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le titre de l'article ne peut pas être vide")
    @Size(max = 50, message = "Le titre de l'article ne peut pas dépasser {max} caractères")
    private String titre;

    @NotNull(message = "Le contenu de l'article ne peut pas être vide")
    @Size(max = 500, message = "Le contenu de l'article ne peut pas dépasser {max} caractères")
    private String contenu;

    @ManyToOne
    @JoinColumn(name="utilisateur_id", referencedColumnName = "id")
    private InfoUtilisateur auteur;

    @ManyToOne
    @JoinColumn(name="theme_id", referencedColumnName = "id")
    private Theme theme;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime dateCreation;
}