package com.citronix.citronix.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Tree {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private LocalDate plantingDate;

    @ManyToOne
    @JoinColumn(name = "field_id", nullable = false)
    private Field field;

    @Transient
    private int age;

    @Transient
    private double productivityPerSeason;

    @OneToMany(mappedBy = "tree")
    private Set<HarvestDetail> harvestDetails = new HashSet<>();
}
