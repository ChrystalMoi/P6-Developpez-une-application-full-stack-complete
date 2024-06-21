package com.openclassrooms.mddapi.payload;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreerUtilisateurRequest {

    @Size(max = 20)
    @NotNull
    private String nom;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,60}$")
    private String motDePasse;

    @Size(max = 50)
    @NotNull
    @Email
    private String email;
}
