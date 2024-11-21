package com.citronix.citronix.services.impl;

import com.citronix.citronix.mappers.HarvestMapper;
import com.citronix.citronix.repositories.HarvestDetailRepository;
import com.citronix.citronix.repositories.HarvestRepository;
import com.citronix.citronix.services.inter.HarvestDetailService;
import com.citronix.citronix.services.inter.HarvestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
