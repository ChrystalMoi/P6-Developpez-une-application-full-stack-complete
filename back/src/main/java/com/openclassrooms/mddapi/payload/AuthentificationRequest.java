package com.openclassrooms.mddapi.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthentificationRequest {

    @NotNull(message = "L'email ne peut pas être nul")
    private String email;

    @NotNull(message = "Le mot de passe ne peut pas être nul")
    private String motDePasse;
}