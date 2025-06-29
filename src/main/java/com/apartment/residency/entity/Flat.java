package com.apartment.residency.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Flat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String blockName;
    private String flatNumber;
    private String type;
    private Double area;
    private String ownershipStatus;
    private Boolean parkingAvailable;

    @ManyToOne
    @JoinColumn(name = "resident_id")
    private Resident resident;

}
