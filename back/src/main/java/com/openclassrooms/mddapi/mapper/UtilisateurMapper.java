package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UtilisateurDto;
import com.openclassrooms.mddapi.entity.InfoUtilisateur;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface UtilisateurMapper extends DtoEntiteMapper<UtilisateurDto, InfoUtilisateur>{
}
