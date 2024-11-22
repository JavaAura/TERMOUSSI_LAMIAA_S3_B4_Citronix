package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.TreeDTO;
import com.citronix.citronix.entities.Field;
import com.citronix.citronix.entities.Tree;
import com.citronix.citronix.exceptions.TreeNotFoundException;
import com.citronix.citronix.mappers.TreeMapper;
import com.citronix.citronix.repositories.FieldRepository;
import com.citronix.citronix.repositories.TreeRepository;
import com.citronix.citronix.services.inter.TreeService;
import com.citronix.citronix.services.validation.TreeValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class TreeServiceImpl implements TreeService {

    private final TreeRepository treeRepository;
    private final FieldRepository fieldRepository;
    private final TreeMapper treeMapper;
    private final TreeValidationService treeValidationService;

    @Autowired
    public TreeServiceImpl(TreeRepository treeRepository, TreeMapper treeMapper, FieldRepository fieldRepository,
                           TreeValidationService treeValidationService) {
        this.treeMapper = treeMapper;
        this.treeRepository = treeRepository;
        this.fieldRepository = fieldRepository;
        this.treeValidationService=treeValidationService;
    }

    @Override
    public TreeDTO saveTree(@Valid TreeDTO treeDTO) {
        // Validate the planting date from the TreeDTO
        treeValidationService.validatePlantingDate(treeDTO.getPlantingDate());
        Tree tree = treeMapper.toEntity(treeDTO);

        tree.setField(fieldRepository.findById(treeDTO.getFieldId()).orElse(null));
        Field field = tree.getField();
        log.info("area of the field" + field.getArea());
        treeValidationService.checkTreeDensity(field, tree);

        int age = treeValidationService.calculateAge(tree);
        tree.setAge(age);
        log.info("age" + tree.getAge());

        double productivityPerSeason = treeValidationService.calculateProductivityPerSeason(age);
        tree.setProductivityPerSeason(productivityPerSeason);
        log.info("productivityPerSeason" + tree.getProductivityPerSeason());

        Tree savedTree = treeRepository.save(tree);
        return treeMapper.toDTO(savedTree);
    }

    @Override
    public Page<TreeDTO> getAllTrees(Pageable pageable) {
        Page<Tree> treePage = treeRepository.findAll(pageable);
        return treePage.map(treeMapper::toDTO);
    }

    @Override
    public TreeDTO getTreeById(Long id) {
        Tree tree = treeRepository.findById(id).orElseThrow(() -> new TreeNotFoundException(id));
        return treeMapper.toDTO(tree);
    }

    @Override
    public TreeDTO updateTree(Long id,@Valid TreeDTO updateTreeDTO) {
        Tree existingTree = treeRepository.findById(id).orElseThrow(() -> new TreeNotFoundException(id));
        LocalDate treeDTOPlantingDate = updateTreeDTO.getPlantingDate();
        treeValidationService.validatePlantingDate(treeDTOPlantingDate);

        treeMapper.updateEntityFromDTO(updateTreeDTO, existingTree);
        int updateTreeAge = treeValidationService.calculateAge(existingTree);
        existingTree.setAge(updateTreeAge);
        log.info("age" + existingTree.getAge());

        double updateTreeProd = treeValidationService.calculateProductivityPerSeason(updateTreeAge);
        existingTree.setProductivityPerSeason(updateTreeProd);
        log.info("productivityPerSeason" + existingTree.getProductivityPerSeason());

        Tree savedTree = treeRepository.save(existingTree);
        return treeMapper.toDTO(savedTree);
    }
    @Override
    public void deleteTree(Long id){
        Tree tree=treeRepository.findById(id).orElseThrow(()->new TreeNotFoundException(id));
        treeRepository.delete(tree);
    }

}

