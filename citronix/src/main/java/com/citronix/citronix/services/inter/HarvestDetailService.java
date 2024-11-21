package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.HarvestDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;

public interface HarvestDetailService {
    HarvestDetailDTO saveHarvestDetail(@Valid HarvestDetailDTO harvestDetailDTO);
    HarvestDetailDTO updateHarvestDetail(@Valid HarvestDetailDTO harvestDetailDTO,Long Id);
    Page<HarvestDetailDTO> getAllHarvestDetails(Pageable pageable);
}
