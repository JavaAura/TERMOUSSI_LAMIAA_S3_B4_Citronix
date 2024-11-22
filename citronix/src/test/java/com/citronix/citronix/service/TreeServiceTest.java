package com.citronix.citronix.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.citronix.citronix.dto.TreeDTO;
import com.citronix.citronix.entities.Field;
import com.citronix.citronix.entities.Tree;
import com.citronix.citronix.mappers.TreeMapper;
import com.citronix.citronix.repositories.FieldRepository;
import com.citronix.citronix.repositories.TreeRepository;
import com.citronix.citronix.services.impl.TreeServiceImpl;
import com.citronix.citronix.services.validation.TreeValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class TreeServiceTest {

    @Mock
    private TreeRepository treeRepository;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private TreeMapper treeMapper;

    @Mock
    private TreeValidationService treeValidationService;

    @InjectMocks
    private TreeServiceImpl treeService;

    private Long treeId = 1L;
    private Long fieldId = 1L;
    private TreeDTO treeDTO;
    private Tree tree;
    private Field field;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        field = Field.builder()
                .id(fieldId)
                .area(100.0)
                .build();

        treeDTO = TreeDTO.builder()
                .id(treeId)
                .plantingDate(LocalDate.now())
                .fieldId(fieldId)
                .build();

        tree = Tree.builder()
                .id(treeId)
                .plantingDate(LocalDate.now())
                .field(field)
                .build();
    }

    @Test
    void saveTree_shouldSaveTreeSuccessfully() {
        // Given
        when(treeMapper.toEntity(treeDTO)).thenReturn(tree);
        when(fieldRepository.findById(fieldId)).thenReturn(Optional.of(field));
        when(treeValidationService.calculateAge(tree)).thenReturn(5);
        when(treeValidationService.calculateProductivityPerSeason(5)).thenReturn(100.0);
        when(treeRepository.save(tree)).thenReturn(tree);
        when(treeMapper.toDTO(tree)).thenReturn(treeDTO);

        // When
        TreeDTO result = treeService.saveTree(treeDTO);

        // Then
        assertNotNull(result);
        assertEquals(treeDTO.getId(), result.getId());
        verify(treeRepository).save(tree);
    }

    @Test
    void getAllTrees_shouldReturnPageOfTrees() {
        // Given
        Page<Tree> treePage = new PageImpl<>(Arrays.asList(tree));
        when(treeRepository.findAll(PageRequest.of(0, 10))).thenReturn(treePage);
        when(treeMapper.toDTO(tree)).thenReturn(treeDTO);

        // When
        Page<TreeDTO> result = treeService.getAllTrees(PageRequest.of(0, 10));

        // Then
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(treeDTO.getId(), result.getContent().get(0).getId());
    }

    @Test
    void getTreeById_shouldReturnTreeDTO() {
        // Given
        when(treeRepository.findById(treeId)).thenReturn(Optional.of(tree));
        when(treeMapper.toDTO(tree)).thenReturn(treeDTO);

        // When
        TreeDTO result = treeService.getTreeById(treeId);

        // Then
        assertNotNull(result);
        assertEquals(treeDTO.getId(), result.getId());
    }

    @Test
    void updateTree_shouldUpdateTreeSuccessfully() {
        // Given
        TreeDTO updatedDTO = TreeDTO.builder()
                .id(treeId)
                .plantingDate(LocalDate.now().minusYears(1))
                .fieldId(fieldId)
                .build();
        Tree updatedTree = tree.toBuilder()
                .plantingDate(LocalDate.now().minusYears(1))
                .build();

        when(treeRepository.findById(treeId)).thenReturn(Optional.of(tree));
        when(treeValidationService.calculateAge(updatedTree)).thenReturn(6);
        when(treeValidationService.calculateProductivityPerSeason(6)).thenReturn(120.0);

        doNothing().when(treeMapper).updateEntityFromDTO(updatedDTO, tree);

        when(treeRepository.save(updatedTree)).thenReturn(updatedTree);
        when(treeMapper.toDTO(updatedTree)).thenReturn(updatedDTO);

        // When
        TreeDTO result = treeService.updateTree(treeId, updatedDTO);

        // Then
        assertNotNull(result);
        assertEquals(updatedDTO.getId(), result.getId());
        verify(treeRepository).save(updatedTree);
    }

    @Test
    void deleteTree_shouldDeleteTreeSuccessfully() {
        // Given
        when(treeRepository.findById(treeId)).thenReturn(Optional.of(tree));

        // When
        treeService.deleteTree(treeId);

        // Then
        verify(treeRepository).delete(tree);
    }
}
