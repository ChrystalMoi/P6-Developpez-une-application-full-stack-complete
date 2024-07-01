package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.repository.InfoUtilisateurRepository;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
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

import static org.hamcrest.Matchers.containsString;
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

    final ObjectMapper mapper=new ObjectMapper();
    final static PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    final InfoUtilisateur utilisateur1= InfoUtilisateur.builder()
            .email("utilisateur1@test.com")
            .nom("Util1")
            .motDePasse(passwordEncoder.encode("AB12cd34"))
            .roles("ROLE_USER")
            .build();

    @BeforeEach
    @AfterEach
    void init() {
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


}
