package com.openclassrooms.mddapi.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtService {
    private final JwtEncoder jwtEncoder;

    /**
     * Constructeur
     *
     * @param jwtEncoder l'encodeur JWT utilisé pour encoder les tokens
     */
    public JwtService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    // Clé secrète utilisée pour signer les tokens JWT
    @Value("${jwt.secretKey}")
    private String secretKey;

    // Durée de validité d'un token JWT (en millisecondes)
    @Value("${jwt.expirationInMs}")
    private long expirationInMs;

    /**
     * Création d'un token JWT avec les réclamations et le nom d'utilisateur spécifié
     *
     * @param reclamation les réclamations à inclure dans le token
     * @param nomutilisateur le nom d'utilisateur pour lequel le token est créé
     * @return le token JWT créé
     */
    private String creationToken(Map<String, Object> reclamation, String nomutilisateur) {
        return Jwts.builder()
                .setClaims(reclamation)
                .setSubject(nomutilisateur)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 60000 * expirationInMs))
                .signWith(getCleSignature(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Génèration d'un token JWT pour le nom d'utilisateur spécifié
     *
     * @param nomutilisateur le nom d'utilisateur pour lequel le token est généré
     * @return le token JWT généré
     */
    public String generateToken(String nomutilisateur) {
        Map<String, Object> reclamation = new HashMap<>();
        return creationToken(reclamation, nomutilisateur);
    }

    /**
     * Retourne la clé utilisée pour signer les tokens JWT
     *
     * @return la clé de signature
     */
    private Key getCleSignature() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Extrait le nom d'utilisateur d'un token JWT
     *
     * @param token le token JWT
     * @return le nom d'utilisateur extrait
     */
    public String extractNomUtilisateur(String token) {
        return extractionReclamation(token, Claims::getSubject);
    }

    /**
     * Extrait la date d'expiration d'un token JWT
     *
     * @param token le token JWT
     * @return la date d'expiration extraite
     */
    public Date extractDateExpiration(String token) {
        return extractionReclamation(token, Claims::getExpiration);
    }

    /**
     * Vérifie si un token JWT est expirer
     *
     * @param token le token JWT
     * @return true si le token est expiré, sinon false
     */
    private Boolean isTokenExpirer(String token) {
        return extractDateExpiration(token).before(new Date());
    }

    /**
     * Valide un token JWT en le comparant avec les détails de l'utilisateur
     *
     * @param token       le token JWT
     * @param userDetails les détails de l'utilisateur
     * @return true si le token est valide et correspond à l'utilisateur, sinon false
     */
    public Boolean valideToken(String token, UserDetails userDetails) {
        final String nom = extractNomUtilisateur(token);
        return (nom.equals(userDetails.getUsername()) && !isTokenExpirer(token));
    }

    /**
     * Extrait toutes les réclamations d'un token JWT
     *
     * @param token le token JWT
     * @return les réclamations extraites
     */
    private Claims extraireToutesReclamations(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getCleSignature())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrait une réclamation spécifique d'un token JWT en utilisant un résolveur de réclamations
     *
     * @param token           le token JWT
     * @param claimsResolver  la fonction utilisée pour résoudre la réclamation
     * @param <T>             le type de la réclamation
     * @return la réclamation extraite
     */
    public <T> T extractionReclamation(String token, Function<Claims, T> claimsResolver) {
        final Claims reclamations = extraireToutesReclamations(token);
        return claimsResolver.apply(reclamations);
    }
}
