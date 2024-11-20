package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.FieldDTO;
import com.citronix.citronix.services.impl.FieldServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
