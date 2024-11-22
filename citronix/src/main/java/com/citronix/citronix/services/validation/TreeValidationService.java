package com.citronix.citronix.services.validation;

import com.citronix.citronix.entities.Field;
import com.citronix.citronix.entities.Tree;
import com.citronix.citronix.repositories.TreeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;

@Service
@Slf4j
public class TreeValidationService {
    private final TreeRepository treeRepository;
    @Autowired
    public TreeValidationService(TreeRepository treeRepository){
        this.treeRepository=treeRepository;
    }

    public void checkTreeDensity(Field field, Tree tree) {
        int maxAllowedTrees = getMaxNumberOfTrees(convertHectaresToSquareMeters(field.getArea()));
        int currentTreeCount = treeRepository.countByFieldId(field.getId());
        log.info("currentTreeCount" + currentTreeCount);
        if (currentTreeCount >= maxAllowedTrees) {
            throw new IllegalArgumentException("The field has too many trees. Maximum allowed: " + maxAllowedTrees);
        }
    }

    public double convertHectaresToSquareMeters(double areaInHectares) {
        log.info("convertHectaresToSquareMeters" + areaInHectares * 10000);
        return areaInHectares * 10000;
    }

    public int getMaxNumberOfTrees(double fieldSizeInM2) {
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
