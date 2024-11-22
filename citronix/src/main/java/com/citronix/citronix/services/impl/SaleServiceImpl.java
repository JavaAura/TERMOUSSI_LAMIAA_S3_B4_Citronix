package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.SaleDTO;
import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.Sale;
import com.citronix.citronix.exceptions.HarvestNotFoundException;
import com.citronix.citronix.exceptions.SaleNotFoundException;
import com.citronix.citronix.mappers.SaleMapper;
import com.citronix.citronix.repositories.HarvestRepository;
import com.citronix.citronix.repositories.SaleRepository;
import com.citronix.citronix.services.inter.SaleService;
import com.citronix.citronix.services.validation.SaleValidationService;
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
    private  final SaleValidationService saleValidationService;

    @Autowired
    public SaleServiceImpl(SaleMapper saleMapper, SaleRepository saleRepository, HarvestRepository harvestRepository,
                           SaleValidationService saleValidationService) {
        this.saleMapper = saleMapper;
        this.saleRepository = saleRepository;
        this.harvestRepository = harvestRepository;
        this.saleValidationService=saleValidationService;
    }

    @Override
    public SaleDTO saveSale(@Valid SaleDTO saleDTO) {
        Harvest harvest = harvestRepository.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException(saleDTO.getHarvestId()));
        Sale sale = saleMapper.toEntity(saleDTO);

        saleValidationService.validateHarvestBeforeSale(harvest);
        saleValidationService.validateSaleDate(sale, harvest);
        sale.setQuantity(harvest.getTotalQte());
        sale.setRevenue(saleValidationService.CalculRevenue(harvest, sale));

        sale.setHarvest(harvest);
        Sale savedSale = saleRepository.save(sale);
        return saleMapper.toDTO(savedSale);
    }

    @Override
    public Page<SaleDTO> getAllSales(Pageable pageable) {
        Page<Sale> salesPage = saleRepository.findAll(pageable);
        return salesPage.map(saleMapper::toDTO);
    }

    @Override
    public SaleDTO updateSale(Long id, @Valid SaleDTO saleDTO) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new SaleNotFoundException(id));
        Harvest harvest = harvestRepository.findById(saleDTO.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException(saleDTO.getHarvestId()));
        saleMapper.updateEntityFromDTO(saleDTO, existingSale);
        saleValidationService.validateSaleDate(existingSale, harvest);

        existingSale.setQuantity(harvest.getTotalQte());
        // Recalculate the revenue
        existingSale.setRevenue(saleValidationService.CalculRevenue(harvest, existingSale));
        Sale updatedSale = saleRepository.save(existingSale);
        return saleMapper.toDTO(updatedSale);
    }

    @Override
    public void deleteSale(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));
        saleRepository.delete(sale);
    }

    @Override
    public SaleDTO getSaleById(Long saleId) {
        Sale sale = saleRepository.findById(saleId)
                .orElseThrow(() -> new SaleNotFoundException(saleId));
        return saleMapper.toDTO(sale);
    }


}
