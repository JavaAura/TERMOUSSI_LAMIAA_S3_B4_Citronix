package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.FarmDTO;
import com.citronix.citronix.entities.Farm;
import com.citronix.citronix.services.impl.FarmServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/farms")
@Validated
public class FarmController {
    private final FarmServiceImpl farmServiceImpl;

    @Autowired
    public FarmController(FarmServiceImpl farmServiceImpl) {
        this.farmServiceImpl = farmServiceImpl;
    }

    @GetMapping
    public ResponseEntity<Page<FarmDTO>> getAllFarms(Pageable pageable) {
        Page<FarmDTO> farmPage = farmServiceImpl.getAllFarms(pageable);
        return ResponseEntity.ok(farmPage);
    }
    @PostMapping
    public ResponseEntity<FarmDTO> saveFarm(@RequestBody @Valid FarmDTO farmDTO) {
        FarmDTO savedFarm = farmServiceImpl.saveFarm(farmDTO);
        return new ResponseEntity<>(savedFarm, HttpStatus.CREATED);
    }
    @PutMapping("/{id}")
    public ResponseEntity<FarmDTO> updateFarm(@PathVariable Long id, @RequestBody @Valid FarmDTO updatedFarmDTO) {
        FarmDTO updatedFarm = farmServiceImpl.updateFarm(id, updatedFarmDTO);
        return ResponseEntity.ok(updatedFarm);
    }
    @GetMapping("/{id}")
    public ResponseEntity<FarmDTO> getFarmById(@PathVariable Long id) {
        FarmDTO farmDTO = farmServiceImpl.getFarmById(id);
        return ResponseEntity.ok(farmDTO);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFarm(@PathVariable Long id) {
        farmServiceImpl.deleteFarm(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<FarmDTO>> searchFarms(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) Double area) {

        List<FarmDTO> farmDTOs = farmServiceImpl.searchFarms(name, location, area);
        return ResponseEntity.ok(farmDTOs);
    }
}
