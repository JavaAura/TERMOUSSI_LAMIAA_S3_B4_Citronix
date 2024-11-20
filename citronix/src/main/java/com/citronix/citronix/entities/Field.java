package com.citronix.citronix.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DecimalMin(value = "0.1", message = "Field area must be at least 0.1 hectare (1000 m²)")
    private double area;

    @ManyToOne
    @JoinColumn(name = "farm_id")
    private Farm farm;
}
