package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.TestContent;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentaireRepository;
import com.openclassrooms.mddapi.repository.InfoUtilisateurRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.service.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArticleControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    InfoUtilisateurRepository infoUtilisateurRepository;
    @Autowired
    InfoUtilisateurServiceImpl infoUtilisateurService;
    @Autowired
    ArticleRepository repository;
    @Autowired
    ArticleServiceImpl articleService;
    @Autowired
    ThemeRepository themeRepository;
    @Autowired
    ThemeServiceImpl themeService;
    @Autowired
    CommentaireRepository commentaireRepository;
    @Autowired
    CommentaireServiceImpl commentaireService;
    @Autowired
    JwtService jwtService;

    final ObjectMapper mapper=new ObjectMapper();

    @BeforeEach
    @AfterEach
    void clean() {
        commentaireRepository.deleteAll();
        repository.deleteAll();
        infoUtilisateurRepository.deleteAll();
        themeRepository.deleteAll();
    }

    @Test
    @DisplayName("Quand je veux obtenir un article, tout est OK")
    void getThemeByIdOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        themeRepository.save(tc.theme1);
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        Long id=repository.save(tc.article1).getId();

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/article/" + id)
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Quand je veux obtenir un article inexistant, il y a une erreur")
    void getThemeByIdErr() throws Exception {
        //Given
        TestContent tc=new TestContent();
        themeRepository.save(tc.theme1);
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        Long id=repository.save(tc.article1).getId()+999999;

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/article/" + id)
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Quand je veux poster un commentaire, tout est Ok")
    void postCommentaireOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        themeRepository.save(tc.theme1);
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        Long id=repository.save(tc.article1).getId();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/article/" + id + "/commentaire")
                        .header("Authorization","Bearer " + jwt)
                        .content("J'adore cet article"))
                //Then
                .andExpect(status().isOk());
        assertThat(repository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Quand je veux poster un commentaire pour un article inexistant, il y a une erreur")
    void postCommentaireErr() throws Exception {
        //Given
        TestContent tc=new TestContent();
        themeRepository.save(tc.theme1);
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        Long id=repository.save(tc.article1).getId()+ 123456;

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/article/" + id + "/commentaire")
                        .header("Authorization","Bearer " + jwt)
                        .content("J'adore cet article"))
                //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Quand je veux récupérer la liste des commentaires d'un article, tout est OK")
    void getCommentaireOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        themeRepository.save(tc.theme1);
        themeRepository.save(tc.theme2);
        infoUtilisateurRepository.save(tc.utilisateur2);
        String jwt= jwtService.generateToken(tc.utilisateur2.getEmail());
        Long id=repository.save(tc.article2).getId();
        repository.save(tc.article3);
        commentaireRepository.save(tc.commentaire1);
        commentaireRepository.save(tc.commentaire2);

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/article/" + id + "/commentaire")
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)));
    }
}
