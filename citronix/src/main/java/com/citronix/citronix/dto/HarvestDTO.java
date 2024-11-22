package com.citronix.citronix.dto;

import com.citronix.citronix.entities.enums.Seasons;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HarvestDTO {
    private Long id;

    private Seasons season;

    private LocalDate date;
    private double totalQte;
}
