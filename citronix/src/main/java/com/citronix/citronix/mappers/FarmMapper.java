package com.citronix.citronix.mappers;

import com.citronix.citronix.dto.FarmDTO;
import com.citronix.citronix.entities.Farm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FarmMapper {
    FarmMapper INSTANCE = Mappers.getMapper(FarmMapper.class);

//    @Mapping(target = "numberOfFields", expression = "java(farm.getFields() != null ? farm.getFields().size() : 0)")
    FarmDTO toDto(Farm farm);

    Farm toEntity(FarmDTO farmDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(FarmDTO farmDTO, @MappingTarget Farm farm);
}
