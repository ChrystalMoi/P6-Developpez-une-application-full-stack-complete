package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.TestContent;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentaireRepository;
import com.openclassrooms.mddapi.repository.InfoUtilisateurRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.service.ArticleServiceImpl;
import com.openclassrooms.mddapi.service.CommentaireServiceImpl;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import com.openclassrooms.mddapi.service.JwtService;
import com.openclassrooms.mddapi.service.ThemeServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    @Autowired
    private ObjectMapper objectMapper;

    // Méthode utilitaire pour convertir un objet en JSON
    private String convertObjectToJson(Object object) throws JsonProcessingException {
        return objectMapper.writeValueAsString(object);
    }

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
                .andExpect(status().isNotFound());
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
        Map<String,String> content=new HashMap<>();
        content.put("contenu","J'adore cet article");

        // Vérification de l'ID de l'article
        System.out.println("ID de l'article enregistré : " + id);

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/article/" + id + "/commentaire")
                        .header("Authorization","Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(content)))
                //Then
                .andExpect(status().isOk());
        System.out.println("Taille de la liste d'articles : " + repository.findAll().size());
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
        Map<String,String> content=new HashMap<>();
        content.put("contenu","J'adore cet article");

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/article/" + id + "/commentaire")
                        .header("Authorization","Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(content)))
                //Then
                .andExpect(status().isNotFound());
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
