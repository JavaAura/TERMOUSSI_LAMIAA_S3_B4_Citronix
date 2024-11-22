package com.citronix.citronix.mappers;

import com.citronix.citronix.dto.SaleDTO;
import com.citronix.citronix.entities.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface SaleMapper {
    @Mapping(source="harvestId",target="harvest.id")
    Sale toEntity(SaleDTO saleDTO);

    @Mapping(source="harvest.id", target="harvestId")
    SaleDTO toDTO(Sale sale);

    @Mapping(source="harvestId",target="harvest.id")
    @Mapping(target = "id",ignore = true)
    void updateEntityFromDTO(SaleDTO saleDTO, @MappingTarget Sale sale);
}
