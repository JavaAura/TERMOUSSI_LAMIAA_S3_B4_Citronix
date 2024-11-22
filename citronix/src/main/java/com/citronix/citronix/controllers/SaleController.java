package com.citronix.citronix.controllers;

import com.citronix.citronix.dto.SaleDTO;
import com.citronix.citronix.services.impl.SaleServiceImpl;
import com.citronix.citronix.services.inter.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/sales")
@Validated
public class SaleController {
    private final SaleServiceImpl saleServiceImpl;

    @Autowired
    public SaleController( SaleServiceImpl saleServiceImpl){
        this.saleServiceImpl=saleServiceImpl;
    }

    @PostMapping
    public ResponseEntity<SaleDTO> saveSale(@RequestBody @Valid SaleDTO saleDTO){
        SaleDTO savedSaleDTO= saleServiceImpl.saveSale(saleDTO);
        return ResponseEntity.ok(savedSaleDTO);
    }

    @GetMapping
    public ResponseEntity<Page<SaleDTO>> getAllSales(Pageable pageable){
        Page<SaleDTO> sales=saleServiceImpl.getAllSales(pageable);
        return ResponseEntity.ok(sales);
    }

}