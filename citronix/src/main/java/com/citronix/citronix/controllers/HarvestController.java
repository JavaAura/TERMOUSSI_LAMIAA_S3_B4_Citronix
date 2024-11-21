package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.HarvestDTO;

import com.citronix.citronix.services.impl.HarvestServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/harvests")
@Validated
public class HarvestController {
    private final HarvestServiceImpl harvestServiceImpl;

    @Autowired
    public HarvestController(HarvestServiceImpl harvestServiceImpl){
        this.harvestServiceImpl=harvestServiceImpl;
    }

    @PostMapping
    public ResponseEntity<HarvestDTO> saveHarvest(@RequestBody @Valid HarvestDTO harvestDTO){
        HarvestDTO savedHarvestDTO = harvestServiceImpl.saveHarvest(harvestDTO);
        return  ResponseEntity.ok(savedHarvestDTO);
    }
}
