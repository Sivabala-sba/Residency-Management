package com.apartment.residency.controller;

import com.apartment.residency.repository.FlatRepository;
import com.apartment.residency.repository.ResidentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final FlatRepository flatRepository;
    private final ResidentRepository residentRepository;

    public HomeController(FlatRepository flatRepository, ResidentRepository residentRepository){
        this.flatRepository = flatRepository;
        this.residentRepository = residentRepository;
    }

    @GetMapping("/")
    public String home(Model model){
        long count1BHK = flatRepository.countByType("1BHK");
        long count2BHK = flatRepository.countByType("2BHK");
        long count3BHK = flatRepository.countByType("3BHK");
        long totalResidents = residentRepository.count();

        model.addAttribute("count1BHK", count1BHK);
        model.addAttribute("count2BHK", count2BHK);
        model.addAttribute("count3BHK", count3BHK);
        model.addAttribute("totalResidents", totalResidents);

        return "index";
    }

}
