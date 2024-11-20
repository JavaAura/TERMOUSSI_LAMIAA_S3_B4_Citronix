package com.citronix.citronix.repositories;

import com.citronix.citronix.entities.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FieldRepository extends JpaRepository<Field,Long> {
    @Query("SELECT COALESCE(SUM(f.area), 0) FROM Field f WHERE f.farm.id = :farmId")
    double sumAreaByFarmId(@Param("farmId") Long farmId);
    long countByFarmId( Long farmId);
}
