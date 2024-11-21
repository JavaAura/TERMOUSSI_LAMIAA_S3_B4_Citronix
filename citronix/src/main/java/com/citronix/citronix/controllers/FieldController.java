package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.FieldDTO;
import com.citronix.citronix.services.impl.FieldServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/fields")
@Validated
public class FieldController {
    private final FieldServiceImpl fieldServiceImpl;

    @Autowired
    public FieldController(FieldServiceImpl fieldServiceImpl){
        this.fieldServiceImpl=fieldServiceImpl;
    }

    @PostMapping
    public ResponseEntity<FieldDTO>saveField(@RequestBody  @Valid FieldDTO fieldDTO){
        FieldDTO savedField= fieldServiceImpl.saveField(fieldDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedField);
    }
    @GetMapping
    public ResponseEntity<Page<FieldDTO>> getAllFields(Pageable pageable){
        Page<FieldDTO> fieldsPage = fieldServiceImpl.getAllFields(pageable);
        return  ResponseEntity.ok(fieldsPage);
    }
    @GetMapping("/{id}")
    public ResponseEntity<FieldDTO> getFieldById(@PathVariable Long id){
        FieldDTO fieldDTO=fieldServiceImpl.getFieldById(id);
        return ResponseEntity.ok(fieldDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<FieldDTO> updateField(@PathVariable Long id, @RequestBody @Valid FieldDTO updatedFieldDTO){
       FieldDTO fieldDTO= fieldServiceImpl.updateField(id,updatedFieldDTO);
        return ResponseEntity.ok(fieldDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteField (@PathVariable Long id){
        fieldServiceImpl.deleteField(id);
        return ResponseEntity.noContent().build();
    }
}
