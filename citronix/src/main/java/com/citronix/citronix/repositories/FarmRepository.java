package com.citronix.citronix.repositories;

import com.citronix.citronix.entities.Farm;
import com.citronix.citronix.repositories.custom.FarmCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmRepository extends JpaRepository<Farm, Long> , FarmCustomRepository {
}
