package com.citronix.citronix.repositories;

import com.citronix.citronix.entities.Tree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TreeRepository extends JpaRepository<Tree,Long> {
    int countByFieldId(Long fieldId);
}
