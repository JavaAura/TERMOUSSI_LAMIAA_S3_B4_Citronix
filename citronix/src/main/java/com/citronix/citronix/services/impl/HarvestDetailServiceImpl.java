package com.citronix.citronix.services.impl;

import com.citronix.citronix.mappers.HarvestDetailMapper;
import com.citronix.citronix.repositories.HarvestDetailRepository;
import com.citronix.citronix.services.inter.HarvestDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HarvestDetailServiceImpl implements HarvestDetailService {

    public final HarvestDetailMapper harvestDetailMapper;
    public final HarvestDetailRepository harvestDetailRepository;

    @Autowired
    public HarvestDetailServiceImpl(HarvestDetailMapper harvestDetailMapper,HarvestDetailRepository harvestDetailRepository){
        this.harvestDetailMapper=harvestDetailMapper;
        this.harvestDetailRepository=harvestDetailRepository;
    }
}
