package com.openclassrooms.mddapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoUtilisateur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le nom ne peut pas être vide")
    @Size(max = 20, message = "Le nom ne peut pas dépasser {max} caractères")
    @Column(unique = true)
    private String nom;

    @NotNull(message = "L'email ne peut pas être vide")
    @Size(max = 50, message = "L'email ne peut pas dépasser {max} caractères")
    @Column(unique = true)
    private String email;

    @NotNull(message = "Le mot de passe ne peut pas être vide")
    //Pas de taille max ici à cause de BCrypt, à gérer dans le payload
    private String motDePasse;

    private String roles;

    @ManyToMany
    @JoinTable(
            name="utilisateur_theme",
            joinColumns = @JoinColumn(name="utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name="theme_id")
    )
    private Set<Theme> subscriptions;
}
