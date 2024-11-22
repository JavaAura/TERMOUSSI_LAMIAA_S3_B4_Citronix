package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.HarvestDTO;
import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.exceptions.HarvestNotFoundException;
import com.citronix.citronix.mappers.HarvestMapper;
import com.citronix.citronix.repositories.HarvestDetailRepository;
import com.citronix.citronix.repositories.HarvestRepository;
import com.citronix.citronix.services.inter.HarvestDetailService;
import com.citronix.citronix.services.inter.HarvestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@Slf4j
public class HarvestServiceImpl implements HarvestService {
    private final HarvestMapper harvestMapper;
    private final HarvestRepository harvestRepository;

    @Autowired
    public HarvestServiceImpl( HarvestMapper harvestMapper,HarvestRepository harvestRepository){
        this.harvestMapper=harvestMapper;
        this.harvestRepository=harvestRepository;
    }
    @Override
    public HarvestDTO saveHarvest(@Valid HarvestDTO harvestDTO){
        Harvest harvest=harvestMapper.toEntity(harvestDTO);
//        harvest.calculateTotalQuantity();
      Harvest savedHarvest=  harvestRepository.save(harvest);
      return harvestMapper.toDTO(savedHarvest);
    }
    @Override
    public Page<HarvestDTO> getAllHarvests(Pageable pageable){
        Page<Harvest> harvests= harvestRepository.findAll(pageable);
        return harvests.map(harvestMapper::toDTO);
    }
    @Override
    public HarvestDTO getHarvestById(Long id){
        Harvest harvest=harvestRepository.findById(id).orElseThrow(()->new HarvestNotFoundException(id));
        return harvestMapper.toDTO(harvest);
    }
    @Override
    public HarvestDTO updateHarvest(Long id, @Valid HarvestDTO harvestDTO){
        Harvest existingHarvest= harvestRepository.findById(id).orElseThrow(()->new HarvestNotFoundException(id));
        harvestMapper.updateEntityFromDTO(harvestDTO,existingHarvest);
        Harvest updatedHarvest=harvestRepository.save(existingHarvest);
        return harvestMapper.toDTO(updatedHarvest);
    }
    @Override
    public void deleteHarvest(Long id){
        Harvest harvest=harvestRepository.findById(id).orElseThrow(()->new HarvestNotFoundException(id));
        harvestRepository.delete(harvest);
    }
}
