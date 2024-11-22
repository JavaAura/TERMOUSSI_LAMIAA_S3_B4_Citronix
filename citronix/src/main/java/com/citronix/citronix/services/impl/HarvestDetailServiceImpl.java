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
import com.citronix.citronix.services.validation.HarvestDetailValidationService;
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

    private final HarvestDetailMapper harvestDetailMapper;
    private final TreeRepository treeRepository;
    private final HarvestDetailRepository harvestDetailRepository;
    private final TreeServiceImpl treeServiceImpl;
    private final HarvestRepository harvestRepository;
    private final HarvestDetailValidationService harvestDetailValidationService;

    @Autowired
    public HarvestDetailServiceImpl(HarvestDetailMapper harvestDetailMapper, HarvestDetailRepository harvestDetailRepository,
                                    TreeRepository treeRepository, TreeServiceImpl treeServiceImpl, HarvestRepository harvestRepository,
                                    HarvestDetailValidationService harvestDetailValidationService) {
        this.harvestDetailMapper = harvestDetailMapper;
        this.harvestDetailRepository = harvestDetailRepository;
        this.treeRepository = treeRepository;
        this.treeServiceImpl = treeServiceImpl;
        this.harvestRepository = harvestRepository;
        this.harvestDetailValidationService = harvestDetailValidationService;
    }

    @Override
    public HarvestDetailDTO saveHarvestDetail(@Valid HarvestDetailDTO harvestDetailDTO) {
        HarvestDetail harvestDetail = harvestDetailMapper.toEntity(harvestDetailDTO);
        log.info("HarvestDetail: {}", harvestDetail);

        // Fetch Harvest with its properties
        Harvest harvest = harvestRepository.findById(harvestDetailDTO.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException(harvestDetailDTO.getHarvestId()));
        harvestDetail.setHarvest(harvest);

        //validate existing tree
        Tree harvestTree = treeRepository.findById(harvestDetailDTO.getTreeId())
                .orElseThrow(() -> new TreeNotFoundException(harvestDetailDTO.getTreeId()));

        //set quantity (tree productivity)
        double harvestTreeProd = treeServiceImpl
                .calculateProductivityPerSeason(treeServiceImpl.calculateAge(harvestTree));
        harvestDetail.setQuantity(harvestTreeProd);

        //validate  harvest per season
        harvestDetailValidationService.validateTreeNotAlreadyHarvested(harvestDetailDTO.getTreeId(), harvest.getSeason(),
                harvest.getDate().getYear());

        HarvestDetail savedHarvestDetail = harvestDetailRepository.save(harvestDetail);
        // Recalculate totalQte for the associated Harvest
        SetCalculHarvestTotalQte(savedHarvestDetail.getHarvest());

        return harvestDetailMapper.toDTO(savedHarvestDetail);
    }

    @Override
    public HarvestDetailDTO updateHarvestDetail(@Valid HarvestDetailDTO harvestDetailDTO, Long id) {
        Tree tree = treeRepository.findById(harvestDetailDTO.getTreeId())
                .orElseThrow(() -> new TreeNotFoundException(harvestDetailDTO.getTreeId()));

        Harvest harvest = harvestRepository.findById(harvestDetailDTO.getHarvestId())
                .orElseThrow(() -> new HarvestNotFoundException(harvestDetailDTO.getHarvestId()));

        // Fetch the existing HarvestDetail record
        HarvestDetail existingHarvestDetail = harvestDetailRepository.findById(id)
                .orElseThrow(() -> new HarvestDetailNotFoundException(id));

        // Update the foreign keys
        existingHarvestDetail.setTree(tree);
        existingHarvestDetail.setHarvest(harvest);

        // Validate if the tree has already been harvested in the same season and year
        harvestDetailValidationService.validateTreeNotAlreadyHarvested(harvestDetailDTO.getTreeId(), harvest.getSeason(),
                harvest.getDate().getYear());

        double newProductivity = treeServiceImpl.calculateProductivityPerSeason(treeServiceImpl.calculateAge(tree));
        existingHarvestDetail.setQuantity(newProductivity);

        HarvestDetail updatedHarvestDetail = harvestDetailRepository.save(existingHarvestDetail);
        SetCalculHarvestTotalQte(harvest);
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
        SetCalculHarvestTotalQte(harvest);
    }

    private void SetCalculHarvestTotalQte(Harvest harvest) {
        List<HarvestDetail> harvestDetails = harvestDetailRepository.findByHarvest(harvest);

        double totalQuantity = 0;
        for (HarvestDetail detail : harvestDetails) {
            totalQuantity += detail.getQuantity();
        }
        harvest.setTotalQte(totalQuantity);
        harvestRepository.save(harvest);
    }


}
