package com.citronix.citronix.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.citronix.citronix.dto.FarmDTO;
import com.citronix.citronix.entities.Farm;
import com.citronix.citronix.exceptions.FarmNotFoundException;
import com.citronix.citronix.mappers.FarmMapper;
import com.citronix.citronix.repositories.FarmRepository;
import com.citronix.citronix.services.impl.FarmServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class FarmServiceTest {

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FarmMapper farmMapper;

    @InjectMocks
    private FarmServiceImpl farmService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveFarm_shouldReturnSavedFarm() {
        FarmDTO farmDTO = new FarmDTO(null, "Farm A", "Location A", 100.0, LocalDate.now());
        Farm farm = new Farm();
        Farm savedFarm = new Farm();
        savedFarm.setId(1L);

        when(farmMapper.toEntity(farmDTO)).thenReturn(farm);
        when(farmRepository.save(farm)).thenReturn(savedFarm);
        when(farmMapper.toDTO(savedFarm)).thenReturn(new FarmDTO(1L, "Farm A", "Location A", 100.0, LocalDate.now()));

        FarmDTO result = farmService.saveFarm(farmDTO);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(farmMapper).toEntity(farmDTO);
        verify(farmRepository).save(farm);
        verify(farmMapper).toDTO(savedFarm);
    }

    @Test
    void getFarmById_shouldReturnFarm_whenFarmExists() {
        // Arrange
        Farm farm = new Farm();
        farm.setId(1L);
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
        when(farmMapper.toDTO(farm)).thenReturn(new FarmDTO(1L, "Farm A", "Location A", 100.0, LocalDate.now()));

        // Act
        FarmDTO result = farmService.getFarmById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(farmRepository).findById(1L);
        verify(farmMapper).toDTO(farm);
    }

    @Test
    void getFarmById_shouldThrowException_whenFarmDoesNotExist() {
        // Arrange
        when(farmRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FarmNotFoundException.class, () -> farmService.getFarmById(1L));
        verify(farmRepository).findById(1L);
        verifyNoInteractions(farmMapper);
    }

    @Test
    void updateFarm_shouldReturnUpdatedFarm_whenFarmExists() {
        // Arrange
        FarmDTO farmDTO = new FarmDTO(null, "Updated Farm", "Updated Location", 200.0, LocalDate.now());
        Farm existingFarm = new Farm();
        existingFarm.setId(1L);

        Farm updatedFarm = new Farm();
        updatedFarm.setId(1L);
        updatedFarm.setName("Updated Farm");

        when(farmRepository.findById(1L)).thenReturn(Optional.of(existingFarm));
        when(farmRepository.save(existingFarm)).thenReturn(updatedFarm);
        when(farmMapper.toDTO(updatedFarm)).thenReturn(new FarmDTO(1L, "Updated Farm", "Updated Location", 200.0, LocalDate.now()));

        // Act
        FarmDTO result = farmService.updateFarm(1L, farmDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Updated Farm", result.getName());
        verify(farmRepository).findById(1L);
        verify(farmRepository).save(existingFarm);
        verify(farmMapper).toDTO(updatedFarm);
    }

    @Test
    void updateFarm_shouldThrowException_whenFarmDoesNotExist() {
        // Arrange
        FarmDTO farmDTO = new FarmDTO();
        when(farmRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FarmNotFoundException.class, () -> farmService.updateFarm(1L, farmDTO));
        verify(farmRepository).findById(1L);
        verifyNoInteractions(farmMapper);
    }

    @Test
    void deleteFarm_shouldDeleteFarm_whenFarmExists() {
        // Arrange
        Farm farm = new Farm();
        farm.setId(1L);
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));

        // Act
        farmService.deleteFarm(1L);

        // Assert
        verify(farmRepository).findById(1L);
        verify(farmRepository).delete(farm);
    }

    @Test
    void deleteFarm_shouldThrowException_whenFarmDoesNotExist() {
        // Arrange
        when(farmRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(FarmNotFoundException.class, () -> farmService.deleteFarm(1L));
        verify(farmRepository).findById(1L);
        verifyNoMoreInteractions(farmRepository);
    }

    @Test
    void searchFarms_shouldReturnFarmsMatchingCriteria() {
        // Arrange
        String name = "Farm A";
        String location = "Location A";
        Double area = 100.0;

        Farm mockFarm = new Farm();
        mockFarm.setId(1L);
        mockFarm.setName("Farm A");
        mockFarm.setLocation("Location A");
        mockFarm.setArea(100.0);
        mockFarm.setCreationDate(LocalDate.now());

        List<Farm> mockFarms = Arrays.asList(mockFarm);
        when(farmRepository.findFarmsByCriteria(name, location, area)).thenReturn(mockFarms);
        when(farmMapper.toDTO(any(Farm.class))).thenReturn(new FarmDTO(1L, "Farm A", "Location A", 100.0, LocalDate.now()));
        // Act
        List<FarmDTO> result = farmService.searchFarms(name, location, area);
        // Assert
        assertEquals(1, result.size());
        assertEquals("Farm A", result.get(0).getName());
    }

    @Test
    void getAllFarms_shouldReturnPaginatedFarms() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Farm farm = new Farm();
        farm.setId(1L);
        farm.setName("Farm A");
        farm.setLocation("Location A");
        farm.setArea(100.0);
        farm.setCreationDate(LocalDate.now());

        List<Farm> farmList = Arrays.asList(farm);
        Page<Farm> farmPage = new PageImpl<>(farmList);

        when(farmRepository.findAll(pageable)).thenReturn(farmPage);
        when(farmMapper.toDTO(farm)).thenReturn(new FarmDTO(1L, "Farm A", "Location A", 100.0, LocalDate.now()));

        // Act
        Page<FarmDTO> result = farmService.getAllFarms(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Farm A", result.getContent().get(0).getName());
        verify(farmRepository).findAll(pageable);
        verify(farmMapper).toDTO(farm);
    }
}
