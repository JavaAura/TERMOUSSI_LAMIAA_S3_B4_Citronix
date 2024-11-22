package com.citronix.citronix.repositories.custom;

import com.citronix.citronix.entities.Farm;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FarmCustomRepositoryImpl implements FarmCustomRepository {
    private final EntityManager entityManager;

    public FarmCustomRepositoryImpl(EntityManager entityManager) {
    this.entityManager=entityManager;
    }

    @Override
    public List<Farm> findFarmsByCriteria(String name, String location, Double area) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Farm> cq = cb.createQuery(Farm.class);
        Root<Farm> root = cq.from(Farm.class);
        List<Predicate> predicates = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(root.get("name"), "%" + name + "%"));
        }
        if (location != null && !location.isEmpty()) {
            predicates.add(cb.equal(root.get("location"), location));
        }
        if (area != null) {
            predicates.add(cb.equal(root.get("area"), area));
        }
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(cq).getResultList();
    }
}
