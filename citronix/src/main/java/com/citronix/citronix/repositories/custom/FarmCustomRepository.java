package com.citronix.citronix.repositories.custom;

import com.citronix.citronix.entities.Farm;
import java.util.List;

public interface FarmCustomRepository {
    List<Farm> findFarmsByCriteria(String name, String location, Double area);

}
