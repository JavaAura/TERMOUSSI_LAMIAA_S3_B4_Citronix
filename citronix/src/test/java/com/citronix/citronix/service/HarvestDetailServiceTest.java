package com.citronix.citronix.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.citronix.citronix.dto.HarvestDetailDTO;
import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.HarvestDetail;
import com.citronix.citronix.entities.Tree;
import com.citronix.citronix.exceptions.HarvestDetailNotFoundException;
import com.citronix.citronix.exceptions.HarvestNotFoundException;
import com.citronix.citronix.mappers.HarvestDetailMapper;
import com.citronix.citronix.repositories.HarvestDetailRepository;
import com.citronix.citronix.repositories.HarvestRepository;
import com.citronix.citronix.repositories.TreeRepository;
import com.citronix.citronix.services.impl.HarvestDetailServiceImpl;
import com.citronix.citronix.services.impl.TreeServiceImpl;
import com.citronix.citronix.services.validation.HarvestDetailValidationService;
import com.citronix.citronix.services.validation.TreeValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

public class HarvestDetailServiceTest {

    @Mock
    private HarvestDetailRepository harvestDetailRepository;

    @Mock
    private HarvestDetailMapper harvestDetailMapper;

    @Mock
    private TreeRepository treeRepository;

    @Mock
    private HarvestRepository harvestRepository;

    @Mock
    private TreeServiceImpl treeServiceImpl;

    @Mock
    private HarvestDetailValidationService harvestDetailValidationService;

    @Mock
    private TreeValidationService treeValidationService;

    @InjectMocks
    private HarvestDetailServiceImpl harvestDetailService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveHarvestDetail_shouldReturnSavedHarvestDetail() {
        HarvestDetailDTO harvestDetailDTO = new HarvestDetailDTO(null, 1L, 1L, 10.0);
        HarvestDetail harvestDetail = new HarvestDetail();
        Harvest savedHarvest = new Harvest();
        savedHarvest.setId(1L);

        when(harvestDetailMapper.toEntity(harvestDetailDTO)).thenReturn(harvestDetail);
        when(harvestRepository.findById(harvestDetailDTO.getHarvestId())).thenReturn(Optional.of(savedHarvest));
        when(treeRepository.findById(harvestDetailDTO.getTreeId())).thenReturn(Optional.of(new Tree()));
        when(harvestDetailRepository.save(harvestDetail)).thenReturn(harvestDetail);
        when(harvestDetailMapper.toDTO(harvestDetail)).thenReturn(harvestDetailDTO);

        HarvestDetailDTO result = harvestDetailService.saveHarvestDetail(harvestDetailDTO);

        assertNotNull(result);
        assertEquals(harvestDetailDTO.getTreeId(), result.getTreeId());
        verify(harvestDetailMapper).toEntity(harvestDetailDTO);
        verify(harvestRepository).findById(harvestDetailDTO.getHarvestId());
        verify(treeRepository).findById(harvestDetailDTO.getTreeId());
        verify(harvestDetailRepository).save(harvestDetail);
        verify(harvestDetailMapper).toDTO(harvestDetail);
    }

    @Test
    void saveHarvestDetail_shouldThrowException_whenHarvestNotFound() {
        HarvestDetailDTO harvestDetailDTO = new HarvestDetailDTO(null, 1L, 1L, 10.0);

        when(harvestRepository.findById(harvestDetailDTO.getHarvestId())).thenReturn(Optional.empty());

        assertThrows(HarvestNotFoundException.class, () -> harvestDetailService.saveHarvestDetail(harvestDetailDTO));
        verify(harvestRepository).findById(harvestDetailDTO.getHarvestId());
    }

    @Test
    void updateHarvestDetail_shouldReturnUpdatedHarvestDetail() {
        HarvestDetailDTO harvestDetailDTO = new HarvestDetailDTO(null, 1L, 1L, 15.0);
        HarvestDetail existingHarvestDetail = new HarvestDetail();
        existingHarvestDetail.setId(1L);
        Tree tree = new Tree();
        Harvest harvest = new Harvest();
        harvest.setId(1L);

        when(harvestRepository.findById(harvestDetailDTO.getHarvestId())).thenReturn(Optional.of(harvest));
        when(treeRepository.findById(harvestDetailDTO.getTreeId())).thenReturn(Optional.of(tree));
        when(harvestDetailRepository.findById(1L)).thenReturn(Optional.of(existingHarvestDetail));
        when(harvestDetailRepository.save(existingHarvestDetail)).thenReturn(existingHarvestDetail);
        when(harvestDetailMapper.toDTO(existingHarvestDetail)).thenReturn(harvestDetailDTO);

        HarvestDetailDTO result = harvestDetailService.updateHarvestDetail(harvestDetailDTO, 1L);

        assertNotNull(result);
        assertEquals(15.0, result.getQuantity());
        verify(harvestRepository).findById(harvestDetailDTO.getHarvestId());
        verify(treeRepository).findById(harvestDetailDTO.getTreeId());
        verify(harvestDetailRepository).findById(1L);
        verify(harvestDetailRepository).save(existingHarvestDetail);
        verify(harvestDetailMapper).toDTO(existingHarvestDetail);
    }

    @Test
    void updateHarvestDetail_shouldThrowException_whenHarvestDetailNotFound() {
        HarvestDetailDTO harvestDetailDTO = new HarvestDetailDTO(null, 1L, 1L, 15.0);

        when(harvestDetailRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HarvestDetailNotFoundException.class, () -> harvestDetailService.updateHarvestDetail(harvestDetailDTO, 1L));
        verify(harvestDetailRepository).findById(1L);
    }

    @Test
    void deleteHarvestDetail_shouldDeleteHarvestDetail_whenExists() {
        HarvestDetail harvestDetail = new HarvestDetail();
        harvestDetail.setId(1L);
        Harvest harvest = new Harvest();
        harvest.setId(1L);
        harvestDetail.setHarvest(harvest);

        when(harvestDetailRepository.findById(1L)).thenReturn(Optional.of(harvestDetail));

        harvestDetailService.deleteHarvestDetail(1L);

        verify(harvestDetailRepository).findById(1L);
        verify(harvestDetailRepository).delete(harvestDetail);
        verify(harvestRepository).save(harvest);
    }

    @Test
    void deleteHarvestDetail_shouldThrowException_whenNotFound() {
        when(harvestDetailRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(HarvestDetailNotFoundException.class, () -> harvestDetailService.deleteHarvestDetail(1L));
        verify(harvestDetailRepository).findById(1L);
    }

    @Test
    void getAllHarvestDetails_shouldReturnPaginatedHarvestDetails() {
        Pageable pageable = PageRequest.of(0, 10);
        HarvestDetail harvestDetail = new HarvestDetail();
        harvestDetail.setId(1L);
        harvestDetail.setQuantity(10.0);

        List<HarvestDetail> harvestDetailsList = Arrays.asList(harvestDetail);
        Page<HarvestDetail> harvestDetailsPage = new PageImpl<>(harvestDetailsList);

        when(harvestDetailRepository.findAll(pageable)).thenReturn(harvestDetailsPage);
        when(harvestDetailMapper.toDTO(harvestDetail)).thenReturn(new HarvestDetailDTO(1L, 1L, 1L, 10.0));

        Page<HarvestDetailDTO> result = harvestDetailService.getAllHarvestDetails(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(10.0, result.getContent().get(0).getQuantity());
        verify(harvestDetailRepository).findAll(pageable);
        verify(harvestDetailMapper).toDTO(harvestDetail);
    }
}
