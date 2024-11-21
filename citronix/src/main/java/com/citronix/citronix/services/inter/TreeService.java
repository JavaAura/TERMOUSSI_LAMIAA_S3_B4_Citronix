package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.TreeDTO;
import org.springframework.data.domain.Page;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface TreeService {
    TreeDTO saveTree(@Valid TreeDTO treeDTO);
    Page<TreeDTO> getAllTrees(Pageable pageable);
    TreeDTO getTreeById(Long id);
}
