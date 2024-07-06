package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.TestContent;
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

import static org.assertj.core.api.Assertions.assertThat;
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
        repository.deleteAll();
        infoUtilisateurRepository.deleteAll();
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


}
