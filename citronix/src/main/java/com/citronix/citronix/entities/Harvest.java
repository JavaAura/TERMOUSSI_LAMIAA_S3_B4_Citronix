package com.citronix.citronix.entities;

import com.citronix.citronix.entities.enums.Seasons;
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
@Builder
public class Harvest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Seasons season;

    @NotNull
    private LocalDate date;

    @OneToMany(mappedBy = "harvest")
    private Set<HarvestDetail> harvestDetails = new HashSet<>();

    //    private double totalQte;
}
