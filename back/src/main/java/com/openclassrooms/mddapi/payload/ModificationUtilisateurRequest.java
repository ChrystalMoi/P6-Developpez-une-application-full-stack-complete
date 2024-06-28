package com.openclassrooms.mddapi.payload;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Représente une requête de modification d'utilisateur avec les détails à mettre à jour
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModificationUtilisateurRequest {
    @Size(max=20)
    private String nom;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,60}$|null|")
    private String motDePasse;

    @Size(max=50)
    @Email
    private String email;
}
