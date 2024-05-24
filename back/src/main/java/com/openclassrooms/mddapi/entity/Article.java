package com.openclassrooms.mddapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(max = 100, message = "Le contenu de l'article ne peut pas dépasser {max} caractères")
    private String contenu;

    @ManyToOne
    @JoinColumn(name="utilisateur_id", referencedColumnName = "id")
    private InfoUtilisateur nomUtilisateur;

    @ManyToOne
    @JoinColumn(name="theme_id", referencedColumnName = "id")
    private Theme theme;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime creeA;
}