package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.FieldDTO;
import com.citronix.citronix.entities.Field;
import com.citronix.citronix.mappers.FarmMapper;
import com.citronix.citronix.mappers.FieldMapper;
import com.citronix.citronix.repositories.FieldRepository;
import com.citronix.citronix.services.inter.FieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Service
public class FieldServiceImpl implements FieldService {
    private final FieldMapper fieldMapper;
    private final FieldRepository fieldRepository;

    @Autowired
    public FieldServiceImpl(FieldMapper fieldMapper,FieldRepository fieldRepository){
        this.fieldMapper=fieldMapper;
        this.fieldRepository=fieldRepository;
    }

    @Override
     public  FieldDTO saveField(@Valid FieldDTO fieldDTO){
        Field field= fieldMapper.toEntity(fieldDTO);
       Field savedField= fieldRepository.save(field);
        return fieldMapper.toDTO(savedField);
    }

}
