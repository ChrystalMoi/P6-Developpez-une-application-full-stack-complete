package com.openclassrooms.mddapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public ResponseEntity<Object> handleArticleNotFoundException(ArticleNotFoundException ex, WebRequest request) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(EmailDejaUtiliseeException.class)
    public ResponseEntity<Object> handleEmailDejaUtiliseeException(EmailDejaUtiliseeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Email déjà utilisé");
    }

    @ExceptionHandler(EntiteNonTrouveeException.class)
    public ResponseEntity<Object> handleEntiteNonTrouveeException(EntiteNonTrouveeException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entite non trouvée");
    }

    @ExceptionHandler(MotDePasseInvalideException.class)
    public ResponseEntity<Object> handleMotDePasseInvalideException(MotDePasseInvalideException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Mot de passe invalide");
    }

    @ExceptionHandler(ErreurGeneriqueException.class)
    protected ResponseEntity<Object> handleGeneric(ErreurGeneriqueException ex) {
        ApiError apiError = new ApiError(BAD_REQUEST);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
}
