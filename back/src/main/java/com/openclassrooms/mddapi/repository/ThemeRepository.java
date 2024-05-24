package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ThemeRepository extends JpaRepository<Theme,Long> {
}
