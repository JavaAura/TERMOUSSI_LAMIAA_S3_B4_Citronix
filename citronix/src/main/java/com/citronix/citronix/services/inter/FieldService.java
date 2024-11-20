package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.FieldDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;


public interface FieldService {
    FieldDTO saveField(@Valid FieldDTO fieldDTO);
    Page<FieldDTO> getAllFields(Pageable pageable);
    FieldDTO getFieldById(Long id);
}
