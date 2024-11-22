package com.citronix.citronix.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.citronix.citronix.dto.FieldDTO;
import com.citronix.citronix.entities.Farm;
import com.citronix.citronix.entities.Field;
import com.citronix.citronix.mappers.FieldMapper;
import com.citronix.citronix.repositories.FarmRepository;
import com.citronix.citronix.repositories.FieldRepository;
import com.citronix.citronix.services.impl.FieldServiceImpl;
import com.citronix.citronix.services.validation.FieldValidationService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

@SpringBootTest
public class FieldServiceTest {

    @Mock
    private FieldMapper fieldMapper;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FieldValidationService fieldValidationService;

    @InjectMocks
    private FieldServiceImpl fieldService;

    @Test
    void saveField_shouldReturnFieldDTO() {
        // Arrange
        Long farmId = 1L;
        Farm farm = new Farm();
        farm.setId(farmId);
        FieldDTO fieldDTO = new FieldDTO();
        fieldDTO.setFarmId(farmId);
        fieldDTO.setArea(20.0); // example area
        Field field = new Field();
        field.setId(1L);
        field.setFarm(farm);

        when(farmRepository.findById(farmId)).thenReturn(Optional.of(farm));
        when(fieldMapper.toEntity(fieldDTO)).thenReturn(field);
        when(fieldRepository.save(field)).thenReturn(field);
        when(fieldMapper.toDTO(field)).thenReturn(new FieldDTO(1L, 20.0, farmId));

        // Act
        FieldDTO result = fieldService.saveField(fieldDTO);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(farmId, result.getFarmId());
        verify(farmRepository).findById(farmId);
        verify(fieldRepository).save(field);
    }

    @Test
    void getAllFields_shouldReturnPaginatedFields() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Field field = new Field();
        field.setId(1L);
        field.setFarm(new Farm());
        Page<Field> fieldPage = new PageImpl<>(Arrays.asList(field));

        when(fieldRepository.findAll(pageable)).thenReturn(fieldPage);
        when(fieldMapper.toDTO(field)).thenReturn(new FieldDTO(1L, 20.0,1L ));

        // Act
        Page<FieldDTO> result = fieldService.getAllFields(pageable);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).getId());
        verify(fieldRepository).findAll(pageable);
        verify(fieldMapper).toDTO(field);
    }

    @Test
    void getFieldById_shouldReturnFieldDTO() {
        // Arrange
        Long id = 1L;
        Field field = new Field();
        field.setId(id);
        field.setFarm(new Farm());
        when(fieldRepository.findById(id)).thenReturn(Optional.of(field));
        when(fieldMapper.toDTO(field)).thenReturn(new FieldDTO(id, 20.0,1L ));

        // Act
        FieldDTO result = fieldService.getFieldById(id);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(fieldRepository).findById(id);
    }

    @Test
    void updateField_shouldReturnUpdatedFieldDTO() {
        // Arrange
        Long id = 1L;
        FieldDTO updatedFieldDTO = new FieldDTO(id, 30.0,1L ); // Updated area
        Field existingField = new Field();
        existingField.setId(id);
        existingField.setFarm(new Farm());

        Farm farm = existingField.getFarm();
        when(fieldRepository.findById(id)).thenReturn(Optional.of(existingField));
        when(fieldMapper.toDTO(existingField)).thenReturn(new FieldDTO(id, 30.0,1L));

        // Simulating field validation
        doNothing().when(fieldValidationService).validateFieldArea(updatedFieldDTO.getArea(), farm);

        // Act
        FieldDTO result = fieldService.updateField(id, updatedFieldDTO);

        // Assert
        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals(updatedFieldDTO.getArea(), result.getArea());
        verify(fieldRepository).findById(id);
        verify(fieldMapper).updateEntityFromDTO(updatedFieldDTO, existingField);
    }

    @Test
    void deleteField_shouldDeleteField() {
        // Arrange
        Long id = 1L;
        Field field = new Field();
        field.setId(id);
        when(fieldRepository.findById(id)).thenReturn(Optional.of(field));

        // Act
        fieldService.deleteField(id);

        // Assert
        verify(fieldRepository).delete(field);
    }
}
