package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.FieldDTO;
import com.citronix.citronix.entities.Farm;
import com.citronix.citronix.entities.Field;
import com.citronix.citronix.exceptions.FarmNotFoundException;
import com.citronix.citronix.exceptions.FieldNotFoundException;
import com.citronix.citronix.mappers.FieldMapper;
import com.citronix.citronix.repositories.FarmRepository;
import com.citronix.citronix.repositories.FieldRepository;
import com.citronix.citronix.services.inter.FieldService;
import com.citronix.citronix.services.validation.FieldValidationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Service
@Slf4j
public class FieldServiceImpl implements FieldService {
    private final FieldMapper fieldMapper;
    private final FarmRepository farmRepository;
    private final FieldRepository fieldRepository;
    private final FieldValidationService fieldValidationService;

    @Autowired
        public FieldServiceImpl(FieldMapper fieldMapper,FieldRepository fieldRepository,
                                FarmRepository farmRepository,FieldValidationService fieldValidationService){
        this.fieldMapper=fieldMapper;
        this.fieldRepository=fieldRepository;
        this.farmRepository=farmRepository;
        this.fieldValidationService=fieldValidationService;
    }

    @Override
     public  FieldDTO saveField(@Valid FieldDTO fieldDTO){
        Farm farm = farmRepository.findById(fieldDTO.getFarmId())
                .orElseThrow(() -> new FarmNotFoundException(fieldDTO.getFarmId()));
        //checking field area<=50% farm area & total fields areas <=farm area & total fields od area <=10
        fieldValidationService.validateFieldArea(fieldDTO.getArea(), farm);
        Field field = fieldMapper.toEntity(fieldDTO);
        field.setFarm(farm);
        fieldRepository.save(field);

        return fieldMapper.toDTO(field);
    }

    @Override
    public Page<FieldDTO> getAllFields(Pageable pageable){
      Page<Field> fieldPage=  fieldRepository.findAll(pageable);
      return fieldPage.map(fieldMapper::toDTO);
    }
    @Override
    public FieldDTO getFieldById(Long id){
       Field field= fieldRepository.findById(id).orElseThrow(()->new FieldNotFoundException(id));
        return fieldMapper.toDTO(field);
    }
    @Override
    public FieldDTO updateField(Long id,@Valid FieldDTO updatedFieldDTO){
        Field existingField=fieldRepository.findById(id).orElseThrow(()->new FieldNotFoundException(id));
        Farm farm = existingField.getFarm();

        fieldValidationService.validateFieldArea(updatedFieldDTO.getArea(), farm);

        fieldMapper.updateEntityFromDTO(updatedFieldDTO,existingField);
        Field updatedField= fieldRepository.save(existingField);
        return fieldMapper.toDTO(updatedField);
    }
    @Override
    public void deleteField(Long id ){
        Field existingField= fieldRepository.findById(id).orElseThrow(()->new FieldNotFoundException(id));
        fieldRepository.delete(existingField);
    }



}
