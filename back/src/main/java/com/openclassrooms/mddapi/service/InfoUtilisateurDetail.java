package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class InfoUtilisateurDetail implements UserDetails {
    private final String nom;
    private final String motDePasse;
    private final List<GrantedAuthority> roles;

    public InfoUtilisateurDetail(InfoUtilisateur infoUtilisateur) {
        nom = infoUtilisateur.getEmail();
        motDePasse = infoUtilisateur.getMotDePasse();
        roles = Arrays.stream(infoUtilisateur.getRoles().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return motDePasse;
    }

    @Override
    public String getUsername() {
        return nom;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
