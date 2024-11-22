package com.citronix.citronix.service;


import com.citronix.citronix.dto.HarvestDTO;
import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.enums.Seasons;
import com.citronix.citronix.exceptions.HarvestNotFoundException;
import com.citronix.citronix.mappers.HarvestMapper;
import com.citronix.citronix.repositories.HarvestRepository;
import com.citronix.citronix.services.impl.HarvestServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HarvestServiceTest {
    @Mock
    private HarvestMapper harvestMapper;

    @Mock
    private HarvestRepository harvestRepository;

    @InjectMocks
    private HarvestServiceImpl harvestService;

    private HarvestDTO harvestDTO;
    private Harvest harvest;
    private Long harvestId = 1L;

    @BeforeEach
    void setUp() {
        harvestDTO = new HarvestDTO(
                harvestId,
                Seasons.SUMMER,
                LocalDate.of(2023, 11, 22),
                100.0
        );
        harvest = new Harvest();
        harvest.setId(harvestId);
        harvest.setSeason(Seasons.SUMMER);
        harvest.setDate(LocalDate.of(2023, 11, 22));
        harvest.setTotalQte(100.0);
    }

    @Test
    void saveHarvest_shouldSaveHarvest() {
        // Given
        when(harvestMapper.toEntity(harvestDTO)).thenReturn(harvest);
        when(harvestRepository.save(harvest)).thenReturn(harvest);
        when(harvestMapper.toDTO(harvest)).thenReturn(harvestDTO);

        // When
        HarvestDTO result = harvestService.saveHarvest(harvestDTO);

        // Then
        assertNotNull(result);
        assertEquals(harvestDTO.getId(), result.getId());
        verify(harvestRepository, times(1)).save(harvest);
    }

    @Test
    void getAllHarvests_shouldReturnPageOfHarvestDTOs() {
        // Given
        Page<Harvest> harvestPage = new PageImpl<>(Arrays.asList(harvest));
        when(harvestRepository.findAll(any(Pageable.class))).thenReturn(harvestPage);
        when(harvestMapper.toDTO(harvest)).thenReturn(harvestDTO);

        // When
        Page<HarvestDTO> result = harvestService.getAllHarvests(Pageable.unpaged());

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(harvestDTO.getId(), result.getContent().get(0).getId());
    }

    @Test
    void getHarvestById_shouldReturnHarvestDTO() {
        // Given
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(harvest));
        when(harvestMapper.toDTO(harvest)).thenReturn(harvestDTO);

        // When
        HarvestDTO result = harvestService.getHarvestById(harvestId);

        // Then
        assertNotNull(result);
        assertEquals(harvestDTO.getId(), result.getId());
    }

    @Test
    void getHarvestById_shouldThrowHarvestNotFoundException_whenHarvestNotFound() {
        // Given
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(HarvestNotFoundException.class, () -> harvestService.getHarvestById(harvestId));
    }

    @Test
    void updateHarvest_shouldUpdateHarvest() {
        // Given
        HarvestDTO updatedDTO = new HarvestDTO(harvestId, Seasons.SUMMER, LocalDate.now(), 200.0);

        Harvest updatedHarvest = new Harvest();
        updatedHarvest.setId(harvestId);
        updatedHarvest.setSeason(Seasons.SUMMER);
        updatedHarvest.setDate(LocalDate.now());
        updatedHarvest.setTotalQte(200.0);

        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(harvest));

        when(harvestMapper.toDTO(updatedHarvest)).thenReturn(updatedDTO);

        doAnswer(invocation -> {
            HarvestDTO dto = invocation.getArgument(0);
            Harvest entity = invocation.getArgument(1);
            entity.setId(dto.getId());
            entity.setSeason(dto.getSeason());
            entity.setDate(dto.getDate());
            entity.setTotalQte(dto.getTotalQte());
            return null;
        }).when(harvestMapper).updateEntityFromDTO(updatedDTO, harvest);

        when(harvestRepository.save(updatedHarvest)).thenReturn(updatedHarvest);

        // When
        HarvestDTO result = harvestService.updateHarvest(harvestId, updatedDTO);

        // Then
        assertNotNull(result);
        assertEquals(updatedDTO.getId(), result.getId());
        assertEquals(updatedDTO.getSeason(), result.getSeason());
        assertEquals(updatedDTO.getDate(), result.getDate());
        assertEquals(updatedDTO.getTotalQte(), result.getTotalQte(), 0.001);
    }

    @Test
    void updateHarvest_shouldThrowHarvestNotFoundException_whenHarvestNotFound() {
        // Given
        HarvestDTO updatedDTO = new HarvestDTO(harvestId, Seasons.SUMMER, LocalDate.now(), 200.0);

        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(HarvestNotFoundException.class, () -> harvestService.updateHarvest(harvestId, updatedDTO));
    }

    @Test
    void deleteHarvest_shouldDeleteHarvest() {
        // Given
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(harvest));

        // When
        harvestService.deleteHarvest(harvestId);

        // Then
        verify(harvestRepository, times(1)).delete(harvest);
    }

    @Test
    void deleteHarvest_shouldThrowHarvestNotFoundException_whenHarvestNotFound() {
        // Given
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(HarvestNotFoundException.class, () -> harvestService.deleteHarvest(harvestId));
    }
}
