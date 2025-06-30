package com.apartment.residency.controller;

import com.apartment.residency.entity.Flat;
import com.apartment.residency.entity.Resident;
import com.apartment.residency.repository.FlatRepository;
import com.apartment.residency.repository.ResidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/flats")
public class FlatController {

    private final FlatRepository flatRepository;
    private final ResidentRepository residentRepository;

    @GetMapping
    public String listFlats(@RequestParam(required = false) String block,
                            @RequestParam(required = false) String ownership,
                            Model model){

        List<Flat> flats;

        if(block != null && ownership != null){
            flats = flatRepository.findByBlockNameAndOwnershipStatus(block, ownership);
        } else if(block != null){
            flats = flatRepository.findByBlockName(block);
        } else if(ownership != null){
            flats = flatRepository.findByOwnershipStatus(ownership);
        } else{
            flats = flatRepository.findAll();
        }

        model.addAttribute("flats", flats);
        return "flats/flat_list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("flat", new Flat());
        return "flats/flat_form";
    }

    @PostMapping("/save")
    public String saveFlat(@ModelAttribute Flat flat,
                           @RequestParam(required = false) Long existingResidentId,
                           @RequestParam(required = false) boolean newResident) {

        boolean owned = "Owned".equalsIgnoreCase(flat.getOwnershipStatus());

        if (owned) {
            if (newResident) {
                Flat savedFlat = flatRepository.save(flat); // Save flat first
                return "redirect:/residents/create?flatId=" + savedFlat.getId();
            } else if (existingResidentId != null) {
                Resident resident = residentRepository.findById(existingResidentId)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid resident ID: " + existingResidentId));
                flat.setResident(resident);
            }
        } else {
            flat.setResident(null);
        }

        flatRepository.save(flat);
        return "redirect:/flats";
    }


    @GetMapping("/view/{id}")
    public String viewFlat(@PathVariable Long id, Model model){
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flat ID: " + id));
        model.addAttribute("flat", flat);

        if("Owned".equalsIgnoreCase(flat.getOwnershipStatus()) && flat.getResident() != null){
            model.addAttribute("resident", flat.getResident());
        }

        return "flats/flat_details";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        Flat flat = flatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid flat ID: " + id));
        model.addAttribute("flat", flat);
        model.addAttribute("residents", residentRepository.findAll());
        return "flats/flat_form";
    }

    @GetMapping("/delete/{id}")
    public String deleteFlat(@PathVariable Long id){
        flatRepository.deleteById(id);
        return "redirect:/flats";
    }

}
