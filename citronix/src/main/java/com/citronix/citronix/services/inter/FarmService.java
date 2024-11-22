package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.FarmDTO;
import com.citronix.citronix.entities.Farm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.validation.Valid;
import java.util.List;

public interface FarmService {
    Page<FarmDTO> getAllFarms(Pageable pageable);
    FarmDTO saveFarm(@Valid FarmDTO farmDTO);
    FarmDTO updateFarm(Long id, @Valid FarmDTO updatedFarmDTO);
    FarmDTO getFarmById(Long id);
    void deleteFarm(Long id);
    List<FarmDTO> searchFarms(String name, String location, Double area);
}
