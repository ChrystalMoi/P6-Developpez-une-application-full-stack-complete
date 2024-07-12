package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.TestContent;
import com.openclassrooms.mddapi.entity.Article;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.InfoUtilisateurRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.service.ArticleServiceImpl;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import com.openclassrooms.mddapi.service.JwtService;
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
    @Autowired
    JwtService jwtService;

    final ObjectMapper mapper=new ObjectMapper();

    @BeforeEach
    @AfterEach
    void init() {
        articleRepository.deleteAll();
        repository.deleteAll();
        themeRepository.deleteAll();
    }

    @Test
    @DisplayName("Quand je réclame les informations d'un utilisateur existant, j'obtiens son DTO")
    void getUtilisateurOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        Long id=repository.save(tc.utilisateur1).getId();
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/utilisateur/" + id)
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("utilisateur1@test.com")))
                .andExpect(content().string(Matchers.not(containsString("motDePasse"))));
    }

    @Test
    @DisplayName("Quand je réclame les informations d'un utilisateur inexistant, j'obtiens une erreur")
    void getUtilisateurErr() throws Exception {
        //Given
        TestContent tc=new TestContent();
        Long id=repository.save(tc.utilisateur1).getId()+100;
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/utilisateur/" + id)
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Quand je réclame la liste des articles des thèmes auxquels un utilisateur est abonné, tout est OK")
    void getArticlesOk() throws Exception {
        //Given
        TestContent tc=new TestContent();

        themeRepository.save(tc.theme1);
        themeRepository.save(tc.theme2);

        repository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        tc.utilisateur2.setSubscriptions(Set.of(tc.theme1));
        Long id=repository.save(tc.utilisateur2).getId();

        articleRepository.save(tc.article1);
        articleRepository.save(tc.article2);
        articleRepository.save(tc.article3);

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/utilisateur/"+ id +"/articles")
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isOk());
    }
}
