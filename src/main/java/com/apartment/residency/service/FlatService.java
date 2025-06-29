package com.apartment.residency.service;

import com.apartment.residency.entity.Flat;
import com.apartment.residency.entity.Resident;
import com.apartment.residency.repository.FlatRepository;
import com.apartment.residency.repository.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlatService {

    private final FlatRepository flatRepository;
    private final ResidentRepository residentRepository;

    public List<Flat> getAllFlats(){
        return flatRepository.findAll();
    }

    public Optional<Flat> getFlatById(Long id){
        return flatRepository.findById(id);
    }

    public List<Flat> filterFlats(String block, String ownership) {
        if (block != null && ownership != null) {
            return flatRepository.findByBlockNameAndOwnershipStatus(block, ownership);
        } else if (block != null) {
            return flatRepository.findByBlockName(block);
        } else if (ownership != null) {
            return flatRepository.findByOwnershipStatus(ownership);
        } else {
            return flatRepository.findAll();
        }
    }

    public void saveFlat(Flat flat, Long existingResidentId, boolean isNewResident) {
        boolean isOwned = "Owned".equalsIgnoreCase(flat.getOwnershipStatus());

        if (isOwned) {
            if (!isNewResident && existingResidentId != null) {
                Resident resident = residentRepository.findById(existingResidentId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid resident ID: " + existingResidentId));
                flat.setResident(resident);
            }
            // Else: Wait for new resident to be created and manually associate it later
        } else {
            flat.setResident(null);
        }

        flatRepository.save(flat);
    }

    public void deleteFlat(Long id) {
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flat ID: " + id));

        // Remove association with resident before deleting
        flat.setResident(null);
        flatRepository.delete(flat);
    }

    public List<Flat> getFlatsByResident(Resident resident) {
        return flatRepository.findByResident(resident);
    }

}
