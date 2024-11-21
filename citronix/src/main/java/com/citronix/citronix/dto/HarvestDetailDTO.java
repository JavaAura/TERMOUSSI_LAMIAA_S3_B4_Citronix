package com.citronix.citronix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HarvestDetailDTO {

    private Long id;

    private Long harvestId;

    private Long treeId;

    private double quantity;

}
