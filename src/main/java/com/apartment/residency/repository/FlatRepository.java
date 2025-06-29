package com.apartment.residency.repository;

import com.apartment.residency.entity.Flat;
import com.apartment.residency.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlatRepository extends JpaRepository<Flat, Long> {
    List<Flat> findByBlockName(String blockName);
    List<Flat> findByOwnershipStatus(String ownershipStatus);
    List<Flat> findByResident(Resident resident);
    List<Flat> findByBlockNameAndOwnershipStatus(String blockName, String ownershipStatus);
}
