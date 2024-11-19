package com.citronix.citronix.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FarmDTO {
    private Long id;
    private String name;
    private String location;
    private double area;
    private LocalDate creationDate;
}
