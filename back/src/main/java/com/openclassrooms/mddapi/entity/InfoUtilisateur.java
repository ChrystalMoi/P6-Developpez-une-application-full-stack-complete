package com.openclassrooms.mddapi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
    @Size(max = 20, message = "Le mot de passe ne peut pas dépasser {max} caractères")
    private String motDePasse;

    @ManyToMany
    @JoinTable(
            name="utilisateur_theme",
            joinColumns = @JoinColumn(name="utilisateur_id"),
            inverseJoinColumns = @JoinColumn(name="theme_id")
    )
    private Set<Theme> subscriptions;
}
