package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.SaleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;

public interface SaleService {
    SaleDTO saveSale(@Valid SaleDTO saleDTO);
    Page<SaleDTO> getAllSales(Pageable pageable);
    SaleDTO updateSale(Long id, @Valid SaleDTO saleDTO);

}
