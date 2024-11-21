package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.HarvestDetailDTO;

import javax.validation.Valid;

public interface HarvestDetailService {
    HarvestDetailDTO saveHarvestDetail(@Valid HarvestDetailDTO harvestDetailDTO);
    HarvestDetailDTO updateHarvestDetail(@Valid HarvestDetailDTO harvestDetailDTO,Long Id);
}
