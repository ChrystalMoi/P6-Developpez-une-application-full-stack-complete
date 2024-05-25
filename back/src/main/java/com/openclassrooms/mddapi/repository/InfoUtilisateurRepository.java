package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InfoUtilisateurRepository extends JpaRepository<InfoUtilisateur,Long> {
    Optional<InfoUtilisateur> findByEmail(String email);
}
