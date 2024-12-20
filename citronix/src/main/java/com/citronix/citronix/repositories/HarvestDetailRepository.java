package com.citronix.citronix.repositories;

import com.citronix.citronix.entities.Harvest;
import java.util.Optional;
import com.citronix.citronix.entities.HarvestDetail;
import com.citronix.citronix.entities.enums.Seasons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HarvestDetailRepository extends JpaRepository<HarvestDetail,Long> {
    @Query("SELECT COUNT(hd) > 0 FROM HarvestDetail hd " +
            "WHERE hd.tree.id = :treeId AND hd.harvest.season = :season " +
            "AND  FUNCTION('YEAR', hd.harvest.date) = :year")
    boolean existsByTreeIdAndHarvestSeasonAndHarvestYear(
            @Param("treeId") Long treeId,
            @Param("season") Seasons season,
            @Param("year") int year);

    List<HarvestDetail> findByHarvest(Harvest harvest);


}
