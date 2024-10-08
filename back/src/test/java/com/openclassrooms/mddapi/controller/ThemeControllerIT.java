package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.TestContent;
import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.entity.Theme;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.InfoUtilisateurRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.service.ArticleServiceImpl;
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

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ThemeControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    InfoUtilisateurRepository infoUtilisateurRepository;
    @Autowired
    InfoUtilisateurServiceImpl infoUtilisateurService;
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    ArticleServiceImpl articleService;
    @Autowired
    ThemeRepository repository;
    @Autowired
    ThemeServiceImpl themeService;
    @Autowired
    JwtService jwtService;

    final ObjectMapper mapper=new ObjectMapper();

    @BeforeEach
    @AfterEach
    void clean() {
        articleRepository.deleteAll();
        infoUtilisateurRepository.deleteAll();
        repository.deleteAll();
    }

    @Test
    @DisplayName("Quand j'essaye d'avoir la liste de tous les thèmes, tout est OK")
    void getAllThemesOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        repository.save(tc.theme1);
        repository.save(tc.theme2);

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/theme")
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Quand quelqu'un veut ajouter un thème, ça marche (hors cadre du projet)")
    void postThemeOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        repository.save(tc.theme1);
        repository.save(tc.theme2);

        Theme theme=Theme.builder()
                .nom("Cuisine")
                .description("Ce thème n'a rien à voir avec MDD")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/theme")
                        .header("Authorization","Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(theme)))
                //Then
                .andExpect(status().isOk());
        assertThat(repository.findAll().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("Quand je veux obtenir  un thème particulier, tout est OK")
    void getThemeByIdOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        repository.save(tc.theme1);
        Long id=repository.save(tc.theme2).getId();

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/theme/" + id)
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Informatique")));
    }

    @Test
    @DisplayName("Quand je veux obtenir  un thème particulier inexistant, il y a une erreur")
    void getThemeByIdErr() throws Exception {
        //Given
        TestContent tc=new TestContent();
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        repository.save(tc.theme1);
        Long id=repository.save(tc.theme2).getId()+23456;

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/theme/" + id)
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Quand je veux créer un article sur un thème, tout est OK")
    void postArticleOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        repository.save(tc.theme1);
        Long id=repository.save(tc.theme2).getId();

        ArticleDto nouvelArticleDto= ArticleDto.builder()
                .titre("Linux")
                .contenu("Linux est un OS")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/theme/" + id + "/articles")
                        .header("Authorization","Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(nouvelArticleDto)))
                //Then
                .andExpect(status().isOk());
        assertThat(articleRepository.findAll().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Quand je veux créer un article sur un thème inexistant, il y a une erreur")
    void postArticleErr() throws Exception {
        //Given
        TestContent tc=new TestContent();
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        repository.save(tc.theme1);
        Long id=repository.save(tc.theme2).getId() + 99999;

        ArticleDto nouvelArticleDto= ArticleDto.builder()
                .titre("Linux")
                .contenu("Linux est un OS")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/theme/" + id + "/articles")
                        .header("Authorization","Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(nouvelArticleDto)))
                //Then
                .andExpect(status().isNotFound());
        assertThat(articleRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Quand je veux créer un article incorrect, il y a une erreur")
    void postArticleErr2() throws Exception {
        //Given
        TestContent tc=new TestContent();
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        repository.save(tc.theme1);
        Long id=repository.save(tc.theme2).getId();

        ArticleDto nouvelArticleDto= ArticleDto.builder()
                .contenu("Un contenu sans titre")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/theme/" + id + "/articles")
                        .header("Authorization","Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(nouvelArticleDto)))
                //Then
                .andExpect(status().isBadRequest());
        assertThat(articleRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("Quand je veux m'abonner à une session (à laquelle je ne suis pas abonné), tout est OK")
    void subscribeOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        Long id=repository.save(tc.theme1).getId();
        repository.save(tc.theme2);

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/theme/" + id + "/subscription")
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("L'abonnement au thème a été réalisé avec succès")));
    }

    @Test
    @DisplayName("Quand je veux m'abonner à une session à laquelle je suis déjà abonné, il y a un Warning")
    void subscribeWarn() throws Exception {
        //Given
        TestContent tc=new TestContent();
        Long id=repository.save(tc.theme1).getId();
        tc.utilisateur1.setSubscriptions(Set.of(tc.theme1));
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        repository.save(tc.theme2);

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/theme/" + id + "/subscription")
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Le thème est déjà abonné")));
    }

    @Test
    @DisplayName("Quand je veux me désabonner d'une session (à laquelle je suis abonné), tout est OK")
    void unubscribeOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        Long id=repository.save(tc.theme1).getId();
        tc.utilisateur1.setSubscriptions(Set.of(tc.theme1));
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        repository.save(tc.theme2);

        //When
        mockMvc.perform(MockMvcRequestBuilders.delete("/theme/" + id + "/subscription")
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Désabonnement réussi")));
    }

    @Test
    @DisplayName("Quand je veux me désabonner d'une session à laquelle je ne suis pas déjà abonné, il y a un Warning")
    void unsubscribeWarn() throws Exception {
        //Given
        TestContent tc=new TestContent();
        infoUtilisateurRepository.save(tc.utilisateur1);
        String jwt= jwtService.generateToken(tc.utilisateur1.getEmail());
        Long id=repository.save(tc.theme1).getId();
        repository.save(tc.theme2);

        //When
        mockMvc.perform(MockMvcRequestBuilders.delete("/theme/" + id + "/subscription")
                        .header("Authorization","Bearer " + jwt))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Le thème n'était pas abonné")));
    }
}
