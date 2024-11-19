package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.FarmDTO;
import com.citronix.citronix.services.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/farms")
public class FarmController {
    private final FarmService farmService;

    @Autowired
    public FarmController(FarmService farmService) {
        this.farmService = farmService;
    }

//    @GetMapping
//    public Page<FarmDTO> getAllFarms(Pageable pageable) {
//        return farmService.getAllFarms(pageable);
//    }
    @GetMapping
    public ResponseEntity<Page<FarmDTO>> getAllFarms(Pageable pageable) {
        Page<FarmDTO> farmPage = farmService.getAllFarms(pageable);
        return ResponseEntity.ok(farmPage);
    }
}
