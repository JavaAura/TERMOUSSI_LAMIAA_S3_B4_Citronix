package com.citronix.citronix.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Farm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Farm name cannot be blank")
    private String name;

    @NotBlank(message = "Farm location cannot be blank")
    private String location;

    @DecimalMin(value = "0.1", message = "Area must be at least 0.1 hectares")
    private double area;

    @PastOrPresent(message = "Creation date must be in the past or today")
    private LocalDate creationDate;

//    @OneToMany(mappedBy = "farm", cascade = CascadeType.ALL, orphanRemoval = true)
//    @Size(max = 10, message = "A farm cannot have more than 10 fields")
//    private List<Field> fields = new ArrayList<>();
}
