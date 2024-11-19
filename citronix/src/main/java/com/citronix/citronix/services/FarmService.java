package com.citronix.citronix.services;

import com.citronix.citronix.dto.FarmDTO;
import com.citronix.citronix.entities.Farm;
import com.citronix.citronix.mappers.FarmMapper;
import com.citronix.citronix.repositories.FarmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class FarmService {
    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;

    @Autowired
    public FarmService(FarmRepository farmRepository,FarmMapper farmMapper) {
        this.farmRepository = farmRepository;
        this.farmMapper=farmMapper;
    }

    public Page<FarmDTO> getAllFarms(Pageable pageable) {
        Page<Farm> farmPage = farmRepository.findAll(pageable);

        return farmPage.map(farmMapper::toDTO);
    }
}
