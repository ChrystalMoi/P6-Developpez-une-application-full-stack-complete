package com.openclassrooms.mddapi;

import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.entity.Theme;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

public class TestContent {
    final static PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    public InfoUtilisateur utilisateur1= InfoUtilisateur.builder()
            .email("utilisateur1@test.com")
            .nom("Util1")
            .motDePasse(passwordEncoder.encode("AB12cd34"))
            .roles("ROLE_USER")
            .build();

    public InfoUtilisateur utilisateur2= InfoUtilisateur.builder()
            .email("utilisateur2@test.com")
            .nom("Util2")
            .motDePasse(passwordEncoder.encode("Aa123456!"))
            .roles("ROLE_USER")
            .build();

    public Theme theme1=Theme.builder()
            .nom("Algorithmique")
            .description("Ca parle d'algorithmique")
            .build();

    public Theme theme2=Theme.builder()
            .nom("Informatique")
            .description("Ca ne parle d'algos")
            .build();

    public Article article1=Article.builder()
            .titre("Art 1")
            .theme(theme1)
            .contenu("Contenu passionnant n°1")
            .nomUtilisateur(utilisateur1)
            .build();

    public Article article2=Article.builder()
            .titre("Art 2")
            .theme(theme2)
            .contenu("Contenu passionnant n°2")
            .nomUtilisateur(utilisateur2)
            .build();

    public Article article3=Article.builder()
            .titre("Art 3")
            .theme(theme1)
            .contenu("Contenu passionnant n°3")
            .nomUtilisateur(utilisateur2)
            .build();
}
