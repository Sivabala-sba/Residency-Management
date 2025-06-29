package com.apartment.residency.service;

import com.apartment.residency.entity.Flat;
import com.apartment.residency.entity.Resident;
import com.apartment.residency.repository.FlatRepository;
import com.apartment.residency.repository.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ResidentService {

    private final ResidentRepository residentRepository;
    private final FlatRepository flatRepository;

    public List<Resident> getAllResidents() {
        return residentRepository.findAll();
    }

    public Optional<Resident> getResidentById(Long id) {
        return residentRepository.findById(id);
    }

    public List<Resident> searchByName(String name) {
        return residentRepository.findByFullNameContainingIgnoreCase(name);
    }

    public List<Resident> filterByOccupancyStatus(String status) {
        return residentRepository.findByOccupancyStatus(status);
    }

    public Resident saveResident(Resident resident, Long flatId) {
        if (resident.getDateOfPurchase() == null) {
            resident.setDateOfPurchase(LocalDate.now());
        }

        Resident saved = residentRepository.save(resident);

        if (flatId != null) {
            Flat flat = flatRepository.findById(flatId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid flat ID: " + flatId));
            flat.setResident(saved);
            flat.setOwnershipStatus("Owned");
            flatRepository.save(flat);
        }

        return saved;
    }

    public void deleteResident(Long id) {
        Resident resident = residentRepository.findById(id)
                .orElse(null);

        if (resident != null) {
            List<Flat> flats = flatRepository.findByResident(resident);
            for (Flat flat : flats) {
                flat.setResident(null);
                flat.setOwnershipStatus("Unowned");
                flatRepository.save(flat);
            }

            residentRepository.deleteById(id);
        }
    }

    public List<Flat> getFlatsByResident(Resident resident) {
        return flatRepository.findByResident(resident);
    }

}
