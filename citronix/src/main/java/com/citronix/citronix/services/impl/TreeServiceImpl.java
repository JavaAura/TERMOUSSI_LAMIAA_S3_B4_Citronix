package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.TreeDTO;
import com.citronix.citronix.entities.Field;
import com.citronix.citronix.entities.Tree;
import com.citronix.citronix.exceptions.TreeNotFoundException;
import com.citronix.citronix.mappers.TreeMapper;
import com.citronix.citronix.repositories.FieldRepository;
import com.citronix.citronix.repositories.TreeRepository;
import com.citronix.citronix.services.inter.TreeService;
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

    @Autowired
    public TreeServiceImpl(TreeRepository treeRepository, TreeMapper treeMapper, FieldRepository fieldRepository) {
        this.treeMapper = treeMapper;
        this.treeRepository = treeRepository;
        this.fieldRepository = fieldRepository;
    }

    @Override
    public TreeDTO saveTree(@Valid TreeDTO treeDTO) {
        // Validate the planting date from the TreeDTO
        validatePlantingDate(treeDTO.getPlantingDate());
        Tree tree = treeMapper.toEntity(treeDTO);

        tree.setField(fieldRepository.findById(treeDTO.getFieldId()).orElse(null));
        Field field = tree.getField();
        log.info("area of the field" + field.getArea());
        checkTreeDensity(field, tree);

        int age = calculateAge(tree);
        tree.setAge(age);
        log.info("age" + tree.getAge());

        double productivityPerSeason = calculateProductivityPerSeason(age);
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
        validatePlantingDate(treeDTOPlantingDate);

        treeMapper.updateEntityFromDTO(updateTreeDTO, existingTree);
        int updateTreeAge = calculateAge(existingTree);
        existingTree.setAge(updateTreeAge);
        log.info("age" + existingTree.getAge());

        double updateTreeProd = calculateProductivityPerSeason(updateTreeAge);
        existingTree.setProductivityPerSeason(updateTreeProd);
        log.info("productivityPerSeason" + existingTree.getProductivityPerSeason());

        Tree savedTree = treeRepository.save(existingTree);
        return treeMapper.toDTO(savedTree);
    }


    private void checkTreeDensity(Field field, Tree tree) {
        int maxAllowedTrees = getMaxNumberOfTrees(convertHectaresToSquareMeters(field.getArea()));
        int currentTreeCount = treeRepository.countByFieldId(field.getId());
        log.info("currentTreeCount" + currentTreeCount);
        if (currentTreeCount >= maxAllowedTrees) {
            throw new IllegalArgumentException("The field has too many trees. Maximum allowed: " + maxAllowedTrees);
        }
    }

    private double convertHectaresToSquareMeters(double areaInHectares) {
        log.info("convertHectaresToSquareMeters" + areaInHectares * 10000);
        return areaInHectares * 10000;
    }

    private int getMaxNumberOfTrees(double fieldSizeInM2) {
        double treesPerSquareMeter = 10 / 1000.0; // 10 trees per (1,000 m²)
        return (int) (fieldSizeInM2 * treesPerSquareMeter);
    }

    public void validatePlantingDate(LocalDate plantingDate) {
        int month = plantingDate.getMonthValue();
        if (month < 3 || month > 5) {
            throw new IllegalArgumentException("Planting date must be between March and May.");
        }
    }

    public double calculateProductivityPerSeason(int age) {
        if (age < 3) {
            return 2.5;
        } else if (age >= 3 && age <= 10) {
            return 12;
        } else if (age > 10 && age <= 20) {
            return 20;
        } else {
            return 0;
        }
    }

    public double calculateProductivityPerYear(int age) {
        double productivityPerSeason = calculateProductivityPerSeason(age);
        return productivityPerSeason * 4;
    }

    public int calculateAge(Tree tree) {
        return Period.between(tree.getPlantingDate(), LocalDate.now()).getYears();
    }
}

