package com.citronix.citronix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleDTO {
    private Long id;
    private LocalDate saleDate;
    private double unitPrice;
    private String clientName;
    private Long harvestId;
    private double quantity;
    private double revenue;
}
