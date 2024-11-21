package com.citronix.citronix.mappers;

import com.citronix.citronix.dto.FarmDTO;
import com.citronix.citronix.entities.Farm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
@Component
public interface FarmMapper {
    FarmDTO toDTO(Farm farm);

    Farm toEntity(FarmDTO farmDTO);

    @Mapping(target = "id", ignore = true)
    void updateEntityFromDTO(FarmDTO farmDTO, @MappingTarget Farm farm);
    List<FarmDTO> toDtoList(List<Farm> farms);
    List<Farm> toEntityList(List<FarmDTO> farmDTOs);
}
