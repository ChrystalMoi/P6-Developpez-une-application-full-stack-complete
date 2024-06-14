package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.exception.EmailDejaUtiliseeException;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;
import com.openclassrooms.mddapi.exception.MotDePasseInvalideException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface InfoUtilisateurService extends UserDetailsService {

    @Override
    default UserDetails loadUserByUsername(String nomUtilisateur) throws UsernameNotFoundException {
        InfoUtilisateur infoUtilisateur = getUtilisateurParNomUtilisateur(nomUtilisateur);
        if (infoUtilisateur == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur: " + nomUtilisateur);
        }
        return new InfoUtilisateurDetail(infoUtilisateur);
    }

    String modifierUtilisateur(InfoUtilisateur userInfo, boolean traiterMotDePasse) throws EmailDejaUtiliseeException, MotDePasseInvalideException;

    String creerUtilisateur(InfoUtilisateur userInfo) throws EmailDejaUtiliseeException, MotDePasseInvalideException;

    InfoUtilisateur getUtilisateurParNomUtilisateur(String username) throws EntiteNonTrouveeException;

    InfoUtilisateur getUtilisateurParId(Long id) throws EntiteNonTrouveeException;
}
