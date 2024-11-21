package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.HarvestDetailDTO;
import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.HarvestDetail;
import com.citronix.citronix.entities.Tree;
import com.citronix.citronix.entities.enums.Seasons;
import com.citronix.citronix.exceptions.HarvestNotFoundException;
import com.citronix.citronix.exceptions.TreeNotFoundException;
import com.citronix.citronix.mappers.HarvestDetailMapper;
import com.citronix.citronix.repositories.HarvestDetailRepository;
import com.citronix.citronix.repositories.HarvestRepository;
import com.citronix.citronix.repositories.TreeRepository;
import com.citronix.citronix.services.inter.HarvestDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
@Slf4j
public class HarvestDetailServiceImpl implements HarvestDetailService {

    public final HarvestDetailMapper harvestDetailMapper;
    public final TreeRepository treeRepository;
    public final HarvestDetailRepository harvestDetailRepository;
    public final TreeServiceImpl treeServiceImpl;
    public  final HarvestRepository harvestRepository;

    @Autowired
    public HarvestDetailServiceImpl(HarvestDetailMapper harvestDetailMapper, HarvestDetailRepository harvestDetailRepository,
                                    TreeRepository treeRepository, TreeServiceImpl treeServiceImpl,HarvestRepository harvestRepository) {
        this.harvestDetailMapper = harvestDetailMapper;
        this.harvestDetailRepository = harvestDetailRepository;
        this.treeRepository = treeRepository;
        this.treeServiceImpl = treeServiceImpl;
        this.harvestRepository=harvestRepository;

    }

    @Override
    public HarvestDetailDTO saveHarvestDetail(@Valid HarvestDetailDTO harvestDetailDTO) {
        HarvestDetail harvestDetail = harvestDetailMapper.toEntity(harvestDetailDTO);
        log.info("HarvestDetail: {}", harvestDetail);

        // Fetch Harvest with its properties
        Long harvestId = harvestDetailDTO.getHarvestId();
        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new HarvestNotFoundException(harvestId));
        harvestDetail.setHarvest(harvest);

        //validate existing tree
        Long harvestTreeId = harvestDetailDTO.getTreeId();
        Tree harvestTree = treeRepository.findById(harvestTreeId).orElseThrow(() -> new TreeNotFoundException(harvestTreeId));
        //calcul tree age
        int harvestTreeAge = treeServiceImpl.calculateAge(harvestTree);
        //set quantity (tree productivity)
        double harvestTreeProd = treeServiceImpl.calculateProductivityPerSeason(harvestTreeAge);
        harvestDetail.setQuantity(harvestTreeProd);
        //validate  harvest per season
        Seasons harvestSeason = harvest.getSeason();
        int harvestYear = harvest.getDate().getYear();
        validateTreeNotAlreadyHarvested(harvestTreeId, harvestSeason, harvestYear);

        HarvestDetail savedHarvestDetail = harvestDetailRepository.save(harvestDetail);
        log.info("Qte saved harvest" + savedHarvestDetail.getQuantity());
        return harvestDetailMapper.toDTO(savedHarvestDetail);
    }

    private void validateTreeNotAlreadyHarvested(Long treeId, Seasons season, int year) {
        boolean alreadyHarvested = harvestDetailRepository.existsByTreeIdAndHarvestSeasonAndHarvestYear(treeId, season, year);
        if (alreadyHarvested) {
            throw new IllegalArgumentException(
                    "The tree with ID " + treeId + " has already been harvested in the " + season + " of " + year);
        }
    }


}
