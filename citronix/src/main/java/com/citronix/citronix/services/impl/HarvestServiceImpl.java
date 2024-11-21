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
    public final HarvestMapper harvestMapper;
    public final HarvestRepository harvestRepository;

    @Autowired
    public HarvestServiceImpl( HarvestMapper harvestMapper,HarvestRepository harvestRepository){
        this.harvestMapper=harvestMapper;
        this.harvestRepository=harvestRepository;
    }
    @Override
    public HarvestDTO saveHarvest(@Valid HarvestDTO harvestDTO){
        Harvest harvest=harvestMapper.toEntity(harvestDTO);
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
}
