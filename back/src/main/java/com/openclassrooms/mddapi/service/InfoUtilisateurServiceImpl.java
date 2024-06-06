package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import com.openclassrooms.mddapi.exception.EmailDejaUtiliseeException;
import com.openclassrooms.mddapi.exception.EntiteNonTrouveeException;
import com.openclassrooms.mddapi.exception.MotDePasseInvalideException;
import com.openclassrooms.mddapi.repository.InfoUtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class InfoUtilisateurServiceImpl implements InfoUtilisateurService {
    @Autowired
    private InfoUtilisateurRepository infoUtilisateur;

    @Autowired
    private PasswordEncoder encoder;

    @Override
    public String modifierUtilisateur(InfoUtilisateur infoUser, boolean traiterMotDePasse) throws EmailDejaUtiliseeException, MotDePasseInvalideException {
        Optional<InfoUtilisateur> userDetail = infoUtilisateur.findByEmail(infoUser.getEmail());

        //Throws Exception if Email is already registered AND it's not an update
        if (userDetail.isPresent()){
            if (Objects.equals(userDetail.get().getEmail(), infoUser.getEmail())) {
                if (!Objects.equals(userDetail.get().getId(), infoUser.getId())) {
                    throw new EmailDejaUtiliseeException();
                }
            }
        }

        //Otherwise it keeps on
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
    public InfoUtilisateur getUserByUsername(String nom) throws EntiteNonTrouveeException {
        return infoUtilisateur.findByEmail(nom)
                .orElseThrow(() -> new EntiteNonTrouveeException(InfoUtilisateur.class,"email",nom));
    }

    @Override
    public InfoUtilisateur getUserById(Long id) throws EntiteNonTrouveeException {
        return infoUtilisateur.findById(id)
                .orElseThrow(() -> new EntiteNonTrouveeException(UserDetails.class,"id",id.toString()));
    }

    @Override
    public UserDetails loadUserByUsername(String nomUtilisateur) throws UsernameNotFoundException {
        Optional<InfoUtilisateur> detailUtilisateur = infoUtilisateur.findByEmail(nomUtilisateur);

        return detailUtilisateur.map(InfoUtilisateurDetail::new)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec le nom d'utilisateur: " + nomUtilisateur));
    }
}
