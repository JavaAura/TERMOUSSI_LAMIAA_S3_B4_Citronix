package com.citronix.citronix.repositories;

import com.citronix.citronix.entities.Farm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FarmRepository extends JpaRepository<Farm, Long> {
}
