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
    TestContent testContent;

    final ObjectMapper mapper=new ObjectMapper();

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
        Long id=repository.save(testContent.utilisateur1).getId();
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
        Long id=repository.save(testContent.utilisateur1).getId()+100;
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

        themeRepository.save(testContent.theme1);
        themeRepository.save(testContent.theme2);

        repository.save(testContent.utilisateur1);
        testContent.utilisateur2.setSubscriptions(Set.of(testContent.theme1));
        Long id=repository.save(testContent.utilisateur2).getId();

        articleRepository.save(testContent.article1);
        articleRepository.save(testContent.article2);
        articleRepository.save(testContent.article3);

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/utilisateur/"+ id +"/articles"))
                //Then
                .andDo(print())
                .andExpect(status().isOk());
    }
}
