package com.citronix.citronix.mappers;

import com.citronix.citronix.dto.FieldDTO;
import com.citronix.citronix.entities.Field;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface FieldMapper {
    @Mapping(source = "farm.id", target = "farmId")
    FieldDTO toDTO(Field field);

    @Mapping(source = "farmId", target = "farm.id")
    Field toEntity(FieldDTO fieldDTO);

    @Mapping(target="id", ignore=true)
    @Mapping(source = "farmId", target = "farm.id")
    void updateEntityFromDTO(FieldDTO fieldDTO, @MappingTarget Field field);
}
