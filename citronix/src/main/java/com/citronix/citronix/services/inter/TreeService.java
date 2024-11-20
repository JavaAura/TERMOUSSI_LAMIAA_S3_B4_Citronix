package com.citronix.citronix.services.inter;

import com.citronix.citronix.dto.TreeDTO;

import javax.validation.Valid;

public interface TreeService {
    TreeDTO saveTree(@Valid TreeDTO treeDTO);
}
