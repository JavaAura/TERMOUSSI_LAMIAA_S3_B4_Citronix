package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.HarvestDTO;

import com.citronix.citronix.services.impl.HarvestServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping
    public ResponseEntity<Page<HarvestDTO>> getAllHarvests(Pageable pageable){
        Page<HarvestDTO> harvests=harvestServiceImpl.getAllHarvests(pageable);
        return ResponseEntity.ok(harvests);
    }
    @GetMapping("/{id}")
    public ResponseEntity<HarvestDTO> gatHarvestById(@PathVariable Long id){
        HarvestDTO harvestDTO=harvestServiceImpl.getHarvestById(id);
        return ResponseEntity.ok(harvestDTO);
    }
    @PutMapping("/{id}")
    public ResponseEntity<HarvestDTO> updateHarvest(@PathVariable Long id,@RequestBody @Valid HarvestDTO updateHarvestDTO){
        HarvestDTO harvestDTO=harvestServiceImpl.updateHarvest(id,updateHarvestDTO);
        return ResponseEntity.ok(harvestDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHarvest(@PathVariable  Long id){
        harvestServiceImpl.deleteHarvest(id);
        return ResponseEntity.noContent().build();
    }
}
