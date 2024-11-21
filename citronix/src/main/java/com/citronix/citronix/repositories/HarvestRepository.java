package com.citronix.citronix.repositories;

import com.citronix.citronix.entities.Harvest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HarvestRepository extends JpaRepository<Harvest,Long> {
}
