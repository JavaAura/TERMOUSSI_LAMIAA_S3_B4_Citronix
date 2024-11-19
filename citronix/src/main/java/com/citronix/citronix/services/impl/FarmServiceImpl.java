package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.FarmDTO;
import com.citronix.citronix.entities.Farm;
import com.citronix.citronix.exceptions.FarmNotFoundException;
import com.citronix.citronix.mappers.FarmMapper;
import com.citronix.citronix.repositories.FarmRepository;
import com.citronix.citronix.services.inter.FarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class FarmServiceImpl implements FarmService {
    private final FarmRepository farmRepository;
    private final FarmMapper farmMapper;

    @Autowired
    public FarmServiceImpl(FarmRepository farmRepository,FarmMapper farmMapper) {
        this.farmRepository = farmRepository;
        this.farmMapper=farmMapper;
    }
    @Override
    public Page<FarmDTO> getAllFarms(Pageable pageable) {
        Page<Farm> farmPage = farmRepository.findAll(pageable);

        return farmPage.map(farmMapper::toDTO);
    }

    @Override
    public FarmDTO saveFarm(@Valid FarmDTO farmDTO) {
        Farm farm = farmMapper.toEntity(farmDTO);
        Farm savedFarm = farmRepository.save(farm);
        return farmMapper.toDTO(savedFarm);
    }
    @Override
    public FarmDTO updateFarm(Long id, @Valid FarmDTO updatedFarmDTO) {
        Farm existingFarm = farmRepository.findById(id)
                .orElseThrow(() -> new FarmNotFoundException(id));
        farmMapper.updateEntityFromDTO(updatedFarmDTO, existingFarm);
        Farm updatedFarm = farmRepository.save(existingFarm);
        return farmMapper.toDTO(updatedFarm);
    }
}
