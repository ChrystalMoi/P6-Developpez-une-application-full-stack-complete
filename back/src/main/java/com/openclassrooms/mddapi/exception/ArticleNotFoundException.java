package com.openclassrooms.mddapi.exception;

public class ArticleNotFoundException extends RuntimeException {

    public ArticleNotFoundException(Class<?> entityClass, String field, String value) {
        super(String.format("%s introuvable avec %s: %s", entityClass.getSimpleName(), field, value));
    }
}
