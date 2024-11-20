package com.citronix.citronix.mappers;

import com.citronix.citronix.dto.TreeDTO;
import com.citronix.citronix.entities.Tree;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface TreeMapper {

    @Mapping(source="fieldID",target="field.id")
    Tree toEntity(TreeDTO treeDTO);

    @Mapping(source="field.id",target="fieldId")
    TreeDTO toDTO(Tree tree);

    @Mapping(target="id", ignore=true)
    @Mapping(source="fieldId",target="field.id")
    void updateEntityFromDTO(TreeDTO treeDTO, @MappingTarget Tree tree);
}
