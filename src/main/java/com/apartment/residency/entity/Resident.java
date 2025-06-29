package com.apartment.residency.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String primaryContact;
    private String email;
    private LocalDate dateOfPurchase;

    @Embedded
    private EmergencyContact emergencyContact;

    private String occupancyStatus;

    @OneToMany(mappedBy = "resident", cascade = CascadeType.ALL)
    private List<Flat> ownedFlats;

}
