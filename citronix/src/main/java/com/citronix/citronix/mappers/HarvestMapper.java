package com.citronix.citronix.mappers;

import com.citronix.citronix.dto.HarvestDTO;
import com.citronix.citronix.entities.Harvest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface HarvestMapper {

    HarvestDTO toDTO(Harvest harvest);

    Harvest toEntity(HarvestDTO harvestDTO);

    @Mapping(target="id",ignore = true)
    void updateEntityFromDTO(HarvestDTO harvestDTO,@MappingTarget Harvest harvest);
}
