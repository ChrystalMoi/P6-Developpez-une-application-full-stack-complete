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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Theme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Le nom du thème ne peut pas être vide")
    @Size(max = 20, message = "Le nom du thème ne peut pas dépasser {max} caractères")
    @Column(unique = true)
    private String nom;

    @Size(max = 500, message = "La description du thème ne peut pas dépasser {max} caractères")
    private String description;
}