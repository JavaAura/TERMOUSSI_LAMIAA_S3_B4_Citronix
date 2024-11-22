package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.SaleDTO;
import com.citronix.citronix.entities.Sale;
import com.citronix.citronix.mappers.SaleMapper;
import com.citronix.citronix.repositories.SaleRepository;
import com.citronix.citronix.services.inter.SaleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@Slf4j
public class SaleServiceImpl implements SaleService {
    private final SaleMapper saleMapper;
    private final SaleRepository saleRepository;

    @Autowired
    public SaleServiceImpl( SaleMapper saleMapper,SaleRepository saleRepository){
        this.saleMapper=saleMapper;
        this.saleRepository=saleRepository;
    }
    @Override
    public SaleDTO saveSale(@Valid SaleDTO saleDTO){
        Sale sale=saleMapper.toEntity(saleDTO);
        Sale savedSale=saleRepository.save(sale);
        return saleMapper.toDTO(savedSale);
    }
    @Override
    public Page<SaleDTO> getAllSales(Pageable pageable){
        Page<Sale> salesPage=saleRepository.findAll(pageable);
        return salesPage.map(saleMapper::toDTO);
    }
}
