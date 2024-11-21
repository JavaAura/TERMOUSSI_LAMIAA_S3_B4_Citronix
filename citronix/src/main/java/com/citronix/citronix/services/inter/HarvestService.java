package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.HarvestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;

public interface HarvestService {
    HarvestDTO saveHarvest(@Valid HarvestDTO harvestDTO);
    Page<HarvestDTO> getAllHarvests(Pageable pageable);
    HarvestDTO getHarvestById(Long id);
    HarvestDTO updateHarvest(Long id, @Valid HarvestDTO harvestDTO);
    void deleteHarvest(Long id);
}
