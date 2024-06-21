package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.exception.EmailDejaUtiliseeException;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;
import com.openclassrooms.mddapi.exception.MotDePasseInvalideException;
import com.openclassrooms.mddapi.repository.InfoUtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InfoUtilisateurServiceImpl implements InfoUtilisateurService {
    private InfoUtilisateurRepository infoUtilisateur;

    private PasswordEncoder encoder;

    @Override
    public String modifierUtilisateur(InfoUtilisateur infoUser, boolean traiterMotDePasse) throws EmailDejaUtiliseeException, MotDePasseInvalideException {
        Optional<InfoUtilisateur> userDetail = infoUtilisateur.findByEmail(infoUser.getEmail());

        if (isUtilisateurPresent(userDetail)) {
            if (isEmailEgal(userDetail.get(), infoUser)) {
                if (isIdDifferent(userDetail.get(), infoUser)) {
                    throw new EmailDejaUtiliseeException();
                }
            }
        }

        if (traiterMotDePasse) infoUser.setMotDePasse(encoder.encode(infoUser.getMotDePasse()));
        infoUtilisateur.save(infoUser);
        return "Utilisateur modifié";
    }

    @Override
    public String creerUtilisateur(InfoUtilisateur infoUser) throws EmailDejaUtiliseeException, MotDePasseInvalideException {
        Optional<InfoUtilisateur> userDetail = infoUtilisateur.findByEmail(infoUser.getEmail());

        if (userDetail.isPresent()){
            throw new EmailDejaUtiliseeException();
        }

        infoUser.setMotDePasse(encoder.encode(infoUser.getMotDePasse()));
        infoUtilisateur.save(infoUser);
        return "Utilisateur ajouté";
    }

    @Override
    public InfoUtilisateur getUtilisateurParNomUtilisateur(String nom) throws EntiteNonTrouveeException {
        return infoUtilisateur.findByEmail(nom)
                .orElseThrow(() -> new EntiteNonTrouveeException(InfoUtilisateur.class,"email",nom));
    }

    @Override
    public InfoUtilisateur getUtilisateurParId(Long id) throws EntiteNonTrouveeException {
        return infoUtilisateur.findById(id)
                .orElseThrow(() -> new EntiteNonTrouveeException(UserDetails.class,"id",id.toString()));
    }

    @Override
    public UserDetails loadUserByUsername(String nomUtilisateur) throws UsernameNotFoundException {
        Optional<InfoUtilisateur> detailUtilisateur = infoUtilisateur.findByEmail(nomUtilisateur);

        return detailUtilisateur.map(InfoUtilisateurDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur: " + nomUtilisateur));
    }

    private boolean isUtilisateurPresent(Optional<InfoUtilisateur> userDetail) {
        return userDetail.isPresent();
    }

    private boolean isEmailEgal(InfoUtilisateur userDetail, InfoUtilisateur infoUser) {
        return Objects.equals(userDetail.getEmail(), infoUser.getEmail());
    }

    private boolean isIdDifferent(InfoUtilisateur userDetail, InfoUtilisateur infoUser) {
        return !Objects.equals(userDetail.getId(), infoUser.getId());
    }
}
