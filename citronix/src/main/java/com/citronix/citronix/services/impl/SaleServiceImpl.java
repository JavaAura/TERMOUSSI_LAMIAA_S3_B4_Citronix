package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.SaleDTO;
import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.Sale;
import com.citronix.citronix.exceptions.HarvestNotFoundException;
import com.citronix.citronix.mappers.SaleMapper;
import com.citronix.citronix.repositories.HarvestRepository;
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
    private final HarvestRepository harvestRepository;

    @Autowired
    public SaleServiceImpl( SaleMapper saleMapper,SaleRepository saleRepository, HarvestRepository harvestRepository){
        this.saleMapper=saleMapper;
        this.saleRepository=saleRepository;
        this.harvestRepository=harvestRepository;
    }
    @Override
    public SaleDTO saveSale(@Valid SaleDTO saleDTO){
        Harvest harvest = harvestRepository.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException(saleDTO.getHarvestId()));
       validateHarvestBeforeSale(harvest);
        Sale sale=saleMapper.toEntity(saleDTO);
        double totalQuantity = harvest.getTotalQte();
        sale.setQuantity(totalQuantity);

        double revenue = totalQuantity * sale.getUnitPrice();
        sale.setRevenue(revenue);

        sale.setHarvest(harvest);
        Sale savedSale=saleRepository.save(sale);
        return saleMapper.toDTO(savedSale);
    }
    @Override
    public Page<SaleDTO> getAllSales(Pageable pageable){
        Page<Sale> salesPage=saleRepository.findAll(pageable);
        return salesPage.map(saleMapper::toDTO);
    }
    private void validateHarvestBeforeSale(Harvest harvest) {
        if (harvest.getSale() != null) {
            throw new IllegalStateException("This harvest has already been sold.");
        }
        if (harvest.getTotalQte() == 0) {
            throw new IllegalStateException("This harvest has not been completed yet and cannot be sold.");
        }
    }
}
