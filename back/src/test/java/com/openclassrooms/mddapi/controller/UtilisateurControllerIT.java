package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.InfoUtilisateurRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.service.ArticleServiceImpl;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import com.openclassrooms.mddapi.service.ThemeServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UtilisateurControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    InfoUtilisateurRepository repository;
    @Autowired
    InfoUtilisateurServiceImpl infoUtilisateurService;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ArticleServiceImpl articleService;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    ThemeServiceImpl themeService;

    final ObjectMapper mapper=new ObjectMapper();
    final static PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    final InfoUtilisateur utilisateur1= InfoUtilisateur.builder()
            .email("utilisateur1@test.com")
            .nom("Util1")
            .motDePasse(passwordEncoder.encode("AB12cd34"))
            .roles("ROLE_USER")
            .build();

    final InfoUtilisateur utilisateur2= InfoUtilisateur.builder()
            .email("utilisateur2@test.com")
            .nom("Util2")
            .motDePasse(passwordEncoder.encode("Aa123456!"))
            .roles("ROLE_USER")
            .build();

    final Theme theme1=Theme.builder()
            .nom("Algorithmique")
            .description("Ca parle d'algorithmique")
            .build();

    final Theme theme2=Theme.builder()
            .nom("Informatique")
            .description("Ca ne parle d'algos")
            .build();

    final Article article1=Article.builder()
            .titre("Art 1")
            .theme(theme1)
            .contenu("Contenu passionnant n°1")
            .nomUtilisateur(utilisateur1)
            .build();

    final Article article2=Article.builder()
            .titre("Art 2")
            .theme(theme2)
            .contenu("Contenu passionnant n°2")
            .nomUtilisateur(utilisateur2)
            .build();

    final Article article3=Article.builder()
            .titre("Art 3")
            .theme(theme1)
            .contenu("Contenu passionnant n°3")
            .nomUtilisateur(utilisateur2)
            .build();

    @BeforeEach
    @AfterEach
    void init() {
        articleRepository.deleteAll();
        themeRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    @DisplayName("Quand je réclame les informations d'un utilisateur existant, j'obtiens son DTO")
    @WithMockUser
    void getUtilisateurOk() throws Exception {
        //Given
        Long id=repository.save(utilisateur1).getId();
        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/utilisateur/" + id))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("utilisateur1@test.com")))
                .andExpect(content().string(Matchers.not(containsString("motDePasse"))));
    }

    @Test
    @DisplayName("Quand je réclame les informations d'un utilisateur inexistant, j'obtiens une erreur")
    @WithMockUser
    void getUtilisateurErr() throws Exception {
        //Given
        Long id=repository.save(utilisateur1).getId()+100;
        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/utilisateur/" + id))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Quand je réclame la liste des articles des thèmes auxquels un utilisateur est abonné, tout est OK")
    @WithMockUser
    void getArticlesOk() throws Exception {
        //Given

        themeRepository.save(theme1);
        themeRepository.save(theme2);

        repository.save(utilisateur1);
        utilisateur2.setSubscriptions(Set.of(theme1));
        Long id=repository.save(utilisateur2).getId();

        articleRepository.save(article1);
        articleRepository.save(article2);
        articleRepository.save(article3);

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/utilisateur/"+ id +"/articles"))
                //Then
                .andDo(print())
                .andExpect(status().isOk());
    }
}
