package com.citronix.citronix.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "sale date cannot be null")
    private LocalDate saleDate;

    @NotNull(message = "unit price cannot be null")
    private double unitPrice;

    @NotNull(message = "client name cannot be null")
    private String clientName;

    @OneToOne
    @JoinColumn(name = "harvest_id", nullable = false, unique = true)
    private Harvest harvest;

    @Column(nullable = false)
    private double revenue;

    @Transient
    private double quantity;

}
