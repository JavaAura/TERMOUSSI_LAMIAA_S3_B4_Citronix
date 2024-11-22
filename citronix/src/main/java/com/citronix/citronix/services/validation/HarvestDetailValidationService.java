package com.citronix.citronix.services.validation;

import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.HarvestDetail;
import com.citronix.citronix.entities.enums.Seasons;
import com.citronix.citronix.repositories.HarvestDetailRepository;
import com.citronix.citronix.repositories.HarvestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HarvestDetailValidationService {
    private final HarvestDetailRepository harvestDetailRepository;

    @Autowired
    public HarvestDetailValidationService(HarvestDetailRepository harvestDetailRepository){
        this.harvestDetailRepository=harvestDetailRepository;
    }

    public void validateTreeNotAlreadyHarvested(Long treeId, Seasons season, int year) {
        boolean alreadyHarvested = harvestDetailRepository.existsByTreeIdAndHarvestSeasonAndHarvestYear(treeId, season, year);
        if (alreadyHarvested) {
            throw new IllegalArgumentException(
                    "The tree with ID " + treeId + " has already been harvested in the " + season + " of " + year);
        }
    }



}
