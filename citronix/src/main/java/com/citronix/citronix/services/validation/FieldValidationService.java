package com.citronix.citronix.services.validation;

import com.citronix.citronix.entities.Farm;
import com.citronix.citronix.repositories.FieldRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FieldValidationService {
    private final FieldRepository fieldRepository;

    @Autowired
    public FieldValidationService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    public void validateFieldArea(double fieldArea, Farm farm) {
        double totalFieldsArea = fieldRepository.sumAreaByFarmId(farm.getId());
        double remainingFarmArea = farm.getArea() - totalFieldsArea;
        long fieldsCount = fieldRepository.countByFarmId(farm.getId());

        // Validate if field area is less than or equal to 50% of the farm area
        if (fieldArea > (0.5 * farm.getArea())) {
            throw new IllegalArgumentException("Field area cannot be more than 50% of the farm area");
        }
        // Validate if field area does not exceed remaining farm area
        if (fieldArea > remainingFarmArea) {
            throw new IllegalArgumentException(String.format("Field area cannot exceed the remaining available area of the farm." +
                    "Remaining area: %.2f hectares", remainingFarmArea));
        }
        // Validate if the farm has more than 10 fields
        if (fieldsCount >= 10) {
            throw new IllegalArgumentException("Farm cannot contain more than 10 fields");
        }
    }
}
