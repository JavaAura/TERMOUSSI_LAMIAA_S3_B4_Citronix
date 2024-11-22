package com.citronix.citronix.services.impl;

import com.citronix.citronix.dto.HarvestDetailDTO;
import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.HarvestDetail;
import com.citronix.citronix.entities.Tree;
import com.citronix.citronix.entities.enums.Seasons;
import com.citronix.citronix.exceptions.HarvestDetailNotFoundException;
import com.citronix.citronix.exceptions.HarvestNotFoundException;
import com.citronix.citronix.exceptions.TreeNotFoundException;
import com.citronix.citronix.mappers.HarvestDetailMapper;
import com.citronix.citronix.repositories.HarvestDetailRepository;
import com.citronix.citronix.repositories.HarvestRepository;
import com.citronix.citronix.repositories.TreeRepository;
import com.citronix.citronix.services.inter.HarvestDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;


@Service
@Slf4j
public class HarvestDetailServiceImpl implements HarvestDetailService {

    public final HarvestDetailMapper harvestDetailMapper;
    public final TreeRepository treeRepository;
    public final HarvestDetailRepository harvestDetailRepository;
    public final TreeServiceImpl treeServiceImpl;
    public final HarvestRepository harvestRepository;

    @Autowired
    public HarvestDetailServiceImpl(HarvestDetailMapper harvestDetailMapper, HarvestDetailRepository harvestDetailRepository,
                                    TreeRepository treeRepository, TreeServiceImpl treeServiceImpl, HarvestRepository harvestRepository) {
        this.harvestDetailMapper = harvestDetailMapper;
        this.harvestDetailRepository = harvestDetailRepository;
        this.treeRepository = treeRepository;
        this.treeServiceImpl = treeServiceImpl;
        this.harvestRepository = harvestRepository;

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
        // Recalculate totalQte for the associated Harvest
        CalculHarvestTotalQte(savedHarvestDetail.getHarvest());

        return harvestDetailMapper.toDTO(savedHarvestDetail);
    }

    private void CalculHarvestTotalQte(Harvest harvest) {
        List<HarvestDetail> harvestDetails = harvestDetailRepository.findByHarvest(harvest);

        double totalQuantity = 0;
        for (HarvestDetail detail : harvestDetails) {
            totalQuantity += detail.getQuantity();
        }
        harvest.setTotalQte(totalQuantity);
        harvestRepository.save(harvest);
    }

    @Override
    public HarvestDetailDTO updateHarvestDetail(@Valid HarvestDetailDTO harvestDetailDTO, Long id) {
        Long treeId = harvestDetailDTO.getTreeId();
        Long harvestId = harvestDetailDTO.getHarvestId();

        // Optionally, validate that the new treeId and harvestId are valid
        Tree tree = treeRepository.findById(treeId)
                .orElseThrow(() -> new TreeNotFoundException(treeId));

        Harvest harvest = harvestRepository.findById(harvestId)
                .orElseThrow(() -> new HarvestNotFoundException(harvestId));

        // Fetch the existing HarvestDetail record
        HarvestDetail existingHarvestDetail = harvestDetailRepository.findById(id)
                .orElseThrow(() -> new HarvestDetailNotFoundException(id));

        // Update the foreign keys  (changing associations)
        existingHarvestDetail.setTree(tree);
        existingHarvestDetail.setHarvest(harvest);

        // Validate if the tree has already been harvested in the same season and year
        Seasons harvestSeason = harvest.getSeason();
        int harvestYear = harvest.getDate().getYear();
        validateTreeNotAlreadyHarvested(treeId, harvestSeason, harvestYear);

        int treeAge = treeServiceImpl.calculateAge(tree);
        double newProductivity = treeServiceImpl.calculateProductivityPerSeason(treeAge);
        existingHarvestDetail.setQuantity(newProductivity);  // Update quantity if needed

        HarvestDetail updatedHarvestDetail = harvestDetailRepository.save(existingHarvestDetail);
        CalculHarvestTotalQte(harvest);
        return harvestDetailMapper.toDTO(updatedHarvestDetail);
    }

    @Override
    public Page<HarvestDetailDTO> getAllHarvestDetails(Pageable pageable) {
        Page<HarvestDetail> harvestDetailsPage = harvestDetailRepository.findAll(pageable);
        return harvestDetailsPage.map(harvestDetailMapper::toDTO);
    }

    @Override
    public void deleteHarvestDetail(Long id) {
        HarvestDetail harvestDetail = harvestDetailRepository.findById(id)
                .orElseThrow(() -> new HarvestDetailNotFoundException(id));
        Harvest harvest = harvestDetail.getHarvest();
        harvestDetailRepository.delete(harvestDetail);
        CalculHarvestTotalQte(harvest);
    }


    private void validateTreeNotAlreadyHarvested(Long treeId, Seasons season, int year) {
        boolean alreadyHarvested = harvestDetailRepository.existsByTreeIdAndHarvestSeasonAndHarvestYear(treeId, season, year);
        if (alreadyHarvested) {
            throw new IllegalArgumentException(
                    "The tree with ID " + treeId + " has already been harvested in the " + season + " of " + year);
        }
    }


}
