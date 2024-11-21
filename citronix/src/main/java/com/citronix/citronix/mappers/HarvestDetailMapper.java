package com.citronix.citronix.mappers;

import com.citronix.citronix.dto.HarvestDetailDTO;
import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.HarvestDetail;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface HarvestDetailMapper {

    @Mapping(source="harvestId", target = "harvest.id")
    @Mapping(source="treeId",target = "tree.id")
    HarvestDetail toEntity(HarvestDetailDTO harvestDetailDTO);

    @Mapping(source="harvest.id",target = "harvestId")
    @Mapping(source="tree.id",target = "treeId")
    HarvestDetailDTO toDTO(HarvestDetail harvestDetail);

    @Mapping(source="harvestId", target = "harvest.id")
    @Mapping(source="treeId",target = "tree.id")
    @Mapping(target = "id",ignore = true)
    void updateEntityFromDTO(HarvestDetailDTO harvestDetailDTO, @MappingTarget HarvestDetail harvestDetail);
}

