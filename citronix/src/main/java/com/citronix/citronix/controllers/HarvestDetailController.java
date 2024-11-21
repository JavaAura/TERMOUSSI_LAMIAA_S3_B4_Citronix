package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.HarvestDetailDTO;
import com.citronix.citronix.services.impl.HarvestDetailServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/harvestDetails")
@Validated
public class HarvestDetailController {
    private final HarvestDetailServiceImpl harvestDetailServiceImpl;

    @Autowired
    public HarvestDetailController( HarvestDetailServiceImpl harvestDetailServiceImpl){
        this.harvestDetailServiceImpl=harvestDetailServiceImpl;
    }
    @PostMapping
    public ResponseEntity<HarvestDetailDTO> saveHarvestDetail(@RequestBody @Valid HarvestDetailDTO harvestDetailDTO){
        HarvestDetailDTO savedHarvestDetailDTO=harvestDetailServiceImpl.saveHarvestDetail(harvestDetailDTO);
        return ResponseEntity.ok(savedHarvestDetailDTO);
    }
}
