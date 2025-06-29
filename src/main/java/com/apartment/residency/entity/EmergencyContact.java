package com.apartment.residency.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmergencyContact {
    private String name;
    private String relationship;
    private String contactNumber;
}
