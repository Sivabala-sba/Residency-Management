package com.apartment.residency.repository;

import com.apartment.residency.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResidentRepository extends JpaRepository<Resident, Long> {
    List<Resident> findByFullNameContainingIgnoreCase(String fullName);
    List<Resident> findByOccupancyStatus(String occupancyStatus);
}
