package com.citronix.citronix.services.validation;

import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.Sale;
import org.springframework.stereotype.Service;

@Service
public class SaleValidationService {

    public void validateSaleDate(Sale sale, Harvest harvest) {
        if (harvest.getDate().isAfter(sale.getSaleDate())) {
            throw new IllegalArgumentException("Sale date should be after harvest date");
        }
    }

    public double CalculRevenue(Harvest harvest, Sale sale) {
        return harvest.getTotalQte() * sale.getUnitPrice();
    }

    public void validateHarvestBeforeSale(Harvest harvest) {
        if (harvest.getSale() != null) {
            throw new IllegalStateException("This harvest has already been sold.");
        }
        if (harvest.getTotalQte() == 0) {
            throw new IllegalStateException("This harvest has not been completed yet and cannot be sold.");
        }
    }


}
