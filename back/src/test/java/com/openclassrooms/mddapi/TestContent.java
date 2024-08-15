package com.openclassrooms.mddapi;

import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.Commentaire;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.entity.Theme;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
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
            .auteur(utilisateur1)
            .build();

    public Article article2=Article.builder()
            .titre("Art 2")
            .theme(theme2)
            .contenu("Contenu passionnant n°2")
            .auteur(utilisateur2)
            .build();

    public Article article3=Article.builder()
            .titre("Art 3")
            .theme(theme1)
            .contenu("Contenu passionnant n°3")
            .auteur(utilisateur2)
            .build();

    public Commentaire commentaire1=Commentaire.builder()
            .article(article2)
            .dateCreation(LocalDateTime.now())
            .auteur(utilisateur2)
            .contenu("J'adore l'article 2")
            .build();

    public Commentaire commentaire2=Commentaire.builder()
            .article(article3)
            .dateCreation(LocalDateTime.now())
            .auteur(utilisateur2)
            .contenu("Je déteste l'article 3")
            .build();
}
