package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.HarvestDTO;

import javax.validation.Valid;

public interface HarvestService {
    HarvestDTO saveHarvest(@Valid HarvestDTO harvestDTO);
}
