package com.citronix.citronix.service;


import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.citronix.citronix.dto.SaleDTO;
import com.citronix.citronix.entities.Harvest;
import com.citronix.citronix.entities.Sale;
import com.citronix.citronix.entities.enums.Seasons;
import com.citronix.citronix.exceptions.HarvestNotFoundException;
import com.citronix.citronix.exceptions.SaleNotFoundException;
import com.citronix.citronix.mappers.SaleMapper;
import com.citronix.citronix.repositories.HarvestRepository;
import com.citronix.citronix.repositories.SaleRepository;
import com.citronix.citronix.services.impl.SaleServiceImpl;
import com.citronix.citronix.services.validation.SaleValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

public class SaleServiceTest {
    @Mock
    private SaleMapper saleMapper;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private HarvestRepository harvestRepository;

    @Mock
    private SaleValidationService saleValidationService;

    @InjectMocks
    private SaleServiceImpl saleService;

    private Long saleId = 1L;
    private Long harvestId = 1L;
    private SaleDTO saleDTO;
    private Sale sale;
    private Harvest harvest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize test objects
        harvest = new Harvest();
        harvest.setId(harvestId);
        harvest.setSeason(Seasons.SUMMER);
        harvest.setDate(LocalDate.now());
        harvest.setTotalQte(1000);
        harvest.setHarvestDetails(new HashSet<>());

        saleDTO = new SaleDTO();
        saleDTO.setId(saleId);
        saleDTO.setHarvestId(harvestId);
        saleDTO.setUnitPrice(500.0);
        saleDTO.setQuantity(100);
        saleDTO.setSaleDate(LocalDate.now());
        saleDTO.setClientName("Client1");

        sale = new Sale();
        sale.setId(saleId);
        sale.setHarvest(harvest);
        sale.setSaleDate(LocalDate.now());
        sale.setUnitPrice(500.0);
        sale.setClientName("Client1");
        sale.setRevenue(500.0);
        sale.setQuantity(100);
    }

    @Test
    void saveSale_shouldSaveSaleSuccessfully() {
        // Given
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(harvest));
        when(saleMapper.toEntity(saleDTO)).thenReturn(sale);
        when(saleValidationService.CalculRevenue(harvest, sale)).thenReturn(500.0);
        when(saleRepository.save(sale)).thenReturn(sale);
        when(saleMapper.toDTO(sale)).thenReturn(saleDTO);

        // When
        SaleDTO result = saleService.saveSale(saleDTO);

        // Then
        assertNotNull(result);
        assertEquals(saleDTO.getId(), result.getId());
        verify(saleRepository).save(sale);
    }

    @Test
    void saveSale_shouldThrowHarvestNotFoundException_whenHarvestNotFound() {
        // Given
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(HarvestNotFoundException.class, () -> saleService.saveSale(saleDTO));
    }

    @Test
    void getAllSales_shouldReturnPageOfSales() {
        // Given
        Page<Sale> salesPage = new PageImpl<>(Arrays.asList(sale));
        when(saleRepository.findAll(any(Pageable.class))).thenReturn(salesPage);
        when(saleMapper.toDTO(sale)).thenReturn(saleDTO);

        // When
        Page<SaleDTO> result = saleService.getAllSales(Pageable.unpaged());

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(saleDTO.getId(), result.getContent().get(0).getId());
    }
    @Test
    void updateSale_shouldUpdateSaleSuccessfully() {
        // Given
        SaleDTO updatedDTO = new SaleDTO();
        updatedDTO.setId(saleId);
        updatedDTO.setHarvestId(harvestId);
        updatedDTO.setUnitPrice(750.0);
        updatedDTO.setQuantity(150);
        updatedDTO.setSaleDate(LocalDate.now());
        updatedDTO.setClientName("Updated Client");

        Sale updatedSale = new Sale();
        updatedSale.setId(saleId);
        updatedSale.setHarvest(harvest);
        updatedSale.setSaleDate(LocalDate.now());
        updatedSale.setUnitPrice(750.0);
        updatedSale.setClientName("Updated Client");
        updatedSale.setRevenue(750.0);
        updatedSale.setQuantity(150);

        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(harvestRepository.findById(harvestId)).thenReturn(Optional.of(harvest));

        doAnswer(invocation -> {
            SaleDTO dto = invocation.getArgument(0);
            Sale saleToUpdate = invocation.getArgument(1);
            saleToUpdate.setClientName(dto.getClientName());
            saleToUpdate.setUnitPrice(dto.getUnitPrice());
            saleToUpdate.setQuantity(dto.getQuantity());
            saleToUpdate.setRevenue(dto.getUnitPrice() * dto.getQuantity());
            saleToUpdate.setSaleDate(dto.getSaleDate());
            return null;
        }).when(saleMapper).updateEntityFromDTO(updatedDTO, sale);

        when(saleValidationService.CalculRevenue(harvest, updatedSale)).thenReturn(750.0);
        when(saleRepository.save(updatedSale)).thenReturn(updatedSale);
        when(saleMapper.toDTO(updatedSale)).thenReturn(updatedDTO);

        // When
        SaleDTO result = saleService.updateSale(saleId, updatedDTO);

        // Then
        assertNotNull(result);
        assertEquals(updatedDTO.getId(), result.getId());
        assertEquals(updatedDTO.getQuantity(), result.getQuantity());
        verify(saleRepository).save(updatedSale);
    }
    @Test
    void updateSale_shouldThrowSaleNotFoundException_whenSaleNotFound() {
        // Given
        SaleDTO updatedDTO = new SaleDTO();
        updatedDTO.setId(saleId);
        updatedDTO.setHarvestId(harvestId);
        updatedDTO.setUnitPrice(750.0);
        updatedDTO.setQuantity(150);
        updatedDTO.setSaleDate(LocalDate.now());
        updatedDTO.setClientName("Updated Client");

        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(SaleNotFoundException.class, () -> saleService.updateSale(saleId, updatedDTO));
    }

    @Test
    void deleteSale_shouldDeleteSaleSuccessfully() {
        // Given
        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));

        // When
        saleService.deleteSale(saleId);

        // Then
        verify(saleRepository).delete(sale);
    }

    @Test
    void deleteSale_shouldThrowSaleNotFoundException_whenSaleNotFound() {
        // Given
        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(SaleNotFoundException.class, () -> saleService.deleteSale(saleId));
    }

    @Test
    void getSaleById_shouldReturnSaleDTO() {
        // Given
        when(saleRepository.findById(saleId)).thenReturn(Optional.of(sale));
        when(saleMapper.toDTO(sale)).thenReturn(saleDTO);

        // When
        SaleDTO result = saleService.getSaleById(saleId);

        // Then
        assertNotNull(result);
        assertEquals(saleDTO.getId(), result.getId());
    }

    @Test
    void getSaleById_shouldThrowSaleNotFoundException_whenSaleNotFound() {
        // Given
        when(saleRepository.findById(saleId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(SaleNotFoundException.class, () -> saleService.getSaleById(saleId));
    }
}
