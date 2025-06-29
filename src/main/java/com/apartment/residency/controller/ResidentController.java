package com.apartment.residency.controller;

import com.apartment.residency.entity.Flat;
import com.apartment.residency.entity.Resident;
import com.apartment.residency.repository.FlatRepository;
import com.apartment.residency.repository.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/residents")
public class ResidentController {

    private final ResidentRepository residentRepository;
    private final FlatRepository flatRepository;

    @GetMapping
    public String listResidents(@RequestParam(required = false) String name,
                                @RequestParam(required = false) String occupancy,
                                Model model){

        List<Resident> residents;

        if(name != null && !name.isEmpty()){
            residents = residentRepository.findByFullNameContainingIgnoreCase(name);
        } else if(occupancy != null){
            residents = residentRepository.findByOccupancyStatus(occupancy);
        } else{
            residents = residentRepository.findAll();
        }

        model.addAttribute("residents", residents);
        return "residents/resident_list";
    }

    @GetMapping("/create")
    public String showCreateForm(@RequestParam(required = false) Long flatId, Model model){
        Resident resident = new Resident();
        if(flatId != null){
            model.addAttribute("flatId", flatId); // pass for mapping on save
        }
        model.addAttribute("resident", resident);
        return "residents/resident_form";
    }

    @PostMapping("/save")
    public String saveResident(@ModelAttribute Resident resident,
                               @RequestParam(required = false) Long flatId){
        if(resident.getDateOfPurchase() == null){
            resident.setDateOfPurchase(LocalDate.now());
        }

        Resident saved = residentRepository.save(resident);

        if(flatId != null){
            Flat flat = flatRepository.findById(flatId)
                    .orElseThrow(() -> new IllegalArgumentException("Invalid flat ID: " + flatId));
            flat.setResident(saved);
            flat.setOwnershipStatus("Owned");
            flatRepository.save(flat);
        }

        return "redirect:/residents";
    }

    @GetMapping("/view/{id}")
    public String viewResident(@PathVariable Long id, Model model){
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid resident ID: " + id));
        List<Flat> flats = flatRepository.findByResident(resident);

        model.addAttribute("resident", resident);
        model.addAttribute("flats", flats);
        return "residents/resident_details";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        Resident resident = residentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid resident ID: " + id));
        model.addAttribute("resident", resident);
        return "residents/resident_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteResident(@PathVariable Long id){
        Resident resident = residentRepository.findById(id)
                .orElse(null);

        if(resident != null){
            //Disassociate all flats before deleting
            List<Flat> flats = flatRepository.findByResident(resident);
            for(Flat flat : flats){
                flat.setResident(null);
                flat.setOwnershipStatus("Unowned");
                flatRepository.save(flat);
            }
            residentRepository.deleteById(id);
        }

        return "redirect:/residents";
    }

}
