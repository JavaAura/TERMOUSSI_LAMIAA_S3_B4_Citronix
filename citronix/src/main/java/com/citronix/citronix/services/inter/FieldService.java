package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.FieldDTO;

import javax.validation.Valid;


public interface FieldService {
    FieldDTO saveField(@Valid FieldDTO fieldDTO);
}
