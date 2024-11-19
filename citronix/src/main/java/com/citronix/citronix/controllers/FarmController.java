package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.FarmDTO;
import com.citronix.citronix.services.impl.FarmServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;


@RestController
@RequestMapping("/farms")
public class FarmController {
    private final FarmServiceImpl farmServiceImpl;

    @Autowired
    public FarmController(FarmServiceImpl farmServiceImpl) {
        this.farmServiceImpl = farmServiceImpl;
    }

//    @GetMapping
//    public Page<FarmDTO> getAllFarms(Pageable pageable) {
//        return farmService.getAllFarms(pageable);
//    }
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
}
