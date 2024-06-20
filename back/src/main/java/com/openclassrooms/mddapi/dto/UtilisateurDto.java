package com.openclassrooms.mddapi.dto;

import com.openclassrooms.mddapi.entity.Theme;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UtilisateurDto{

    private Long id;

    @NotNull
    @Size(max=20)
    private String nom;

    @NotNull
    @Size(max=50)
    @Email
    private String email;

    private Set<Theme> subscriptions;
}
