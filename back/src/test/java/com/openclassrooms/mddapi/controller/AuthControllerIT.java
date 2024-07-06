package com.openclassrooms.mddapi.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.mddapi.TestContent;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.payload.AuthentificationRequest;
import com.openclassrooms.mddapi.payload.CreerUtilisateurRequest;
import com.openclassrooms.mddapi.payload.ModificationUtilisateurRequest;
import com.openclassrooms.mddapi.repository.InfoUtilisateurRepository;
import com.openclassrooms.mddapi.service.InfoUtilisateurServiceImpl;
import com.openclassrooms.mddapi.service.JwtService;
import org.hamcrest.CoreMatchers;
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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerIT {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    InfoUtilisateurRepository repository;
    @Autowired
    InfoUtilisateurServiceImpl infoUtilisateurService;
    @Autowired
    JwtService jwtService;

    final ObjectMapper mapper=new ObjectMapper();

    @BeforeEach
    @AfterEach
    void clean() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("Quand j'essaye d'ajouter un utilisateur correct, tout est OK")
    void creerUtilisateurOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        CreerUtilisateurRequest request= CreerUtilisateurRequest.builder()
                .nom("Util2")
                .email("utilisateur2@test.com")
                .motDePasse("Aa123456!")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Quand j'essaye d'ajouter un utilisateur avec une adresse qui existe déjà, il y a une erreur")
    void creerUtilisateurErr() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        CreerUtilisateurRequest request= CreerUtilisateurRequest.builder()
                .nom("Util1")
                .email("utilisateur1@test.com")
                .motDePasse("Aa123456!")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Quand j'essaye d'ajouter un utilisateur avec un mot de passe incorrect, il y a une erreur")
    void creerUtilisateurErr2() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        CreerUtilisateurRequest request= CreerUtilisateurRequest.builder()
                .nom("Util2")
                .email("utilisateur2@test.com")
                .motDePasse("z")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Quand j'essaye de me logguer avec les bons identifiants, tout est OK")
    void logUtilisateurOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        AuthentificationRequest request= AuthentificationRequest.builder()
                .email("utilisateur1@test.com")
                .motDePasse("AB12cd34")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("token")));
    }

    @Test
    @DisplayName("Quand j'essaye de me logguer avec un mauvais mot de passe, il y a une erreur")
    void logUtilisateurErr() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        AuthentificationRequest request= AuthentificationRequest.builder()
                .email("utilisateur1@test.com")
                .motDePasse("motDePasseErreur42!")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Quand j'essaye de me logguer avec un mauvais email, il y a une erreur")
    void logUtilisateurErr2() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        AuthentificationRequest request= AuthentificationRequest.builder()
                .email("utilisateurInexistant@test.com")
                .motDePasse("AB12cd34")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login" )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Quand j'essaye d'obtenir mes informations, j'obtiens mon Dto")
    void meUtilisateurOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        repository.save(tc.utilisateur2);
        String jwt= jwtService.generateToken(tc.utilisateur2.getEmail());

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/me" )
                        .header("Authorization","Bearer "+jwt))
                //Then
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Util2")))
                .andExpect(content().string(CoreMatchers.not(containsString("motDePasse"))));
    }

    @Test
    @DisplayName("Quand j'essaye d'obtenir des informations sans jwt, il y a une erreur")
    void meUtilisateurErr() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        repository.save(tc.utilisateur2);
        String jwt= jwtService.generateToken(tc.utilisateur2.getEmail());

        //When
        mockMvc.perform(MockMvcRequestBuilders.get("/auth/me" ))
                //Then
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Quand j'essaye de modifier mes informations, tout est OK")
    void patchUtilisateurOk() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        repository.save(tc.utilisateur2);
        String jwt= jwtService.generateToken(tc.utilisateur2.getEmail());

        ModificationUtilisateurRequest request= ModificationUtilisateurRequest.builder()
                .nom("Util42")
                .motDePasse("AZERtyui123456")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.put("/auth/me")
                        .header("Authorization","Bearer "+jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isOk());

        Optional<InfoUtilisateur> candidate=repository.findByEmail("utilisateur2@test.com");
        assertThat(candidate).isPresent();
        InfoUtilisateur user=candidate.get();
        assertThat(user.getNom()).isEqualTo("Util42");
        assertThat(user.getMotDePasse()).doesNotContain("AZERtyui123456");
    }

    @Test
    @DisplayName("Quand j'essaye de modifier mes informations avec un email déjà pris, il y a une erreur")
    void patchUtilisateurErr() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        repository.save(tc.utilisateur2);
        String jwt = jwtService.generateToken(tc.utilisateur2.getEmail());

        ModificationUtilisateurRequest request = ModificationUtilisateurRequest.builder()
                .email("utilisateur1@test.com")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.put("/auth/me")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Quand j'essaye de modifier mes informations avec des données incorrectes, il y a une erreur")
    void patchUtilisateurErr2() throws Exception {
        //Given
        TestContent tc=new TestContent();
        repository.save(tc.utilisateur1);
        repository.save(tc.utilisateur2);
        String jwt = jwtService.generateToken(tc.utilisateur2.getEmail());

        ModificationUtilisateurRequest request = ModificationUtilisateurRequest.builder()
                .email("utilisateur1")
                .build();

        //When
        mockMvc.perform(MockMvcRequestBuilders.put("/auth/me")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                //Then
                .andExpect(status().isBadRequest());
    }
}
