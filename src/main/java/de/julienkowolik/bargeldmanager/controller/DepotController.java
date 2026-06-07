package de.julienkowolik.bargeldmanager.controller;

import de.julienkowolik.bargeldmanager.entity.BenutzerEntity;
import de.julienkowolik.bargeldmanager.entity.DepotEntity;
import de.julienkowolik.bargeldmanager.model.BargeldArt;
import de.julienkowolik.bargeldmanager.repository.BenutzerRepository;
import de.julienkowolik.bargeldmanager.service.BargeldService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.EnumMap;
import java.util.Map;

@Controller
public class DepotController {

    private final BenutzerRepository benutzerRepository;
    private final BargeldService bargeldService;

    public DepotController(BenutzerRepository benutzerRepository,
                           BargeldService bargeldService) {
        this.benutzerRepository = benutzerRepository;
        this.bargeldService = bargeldService;
    }

    @GetMapping("/depot")
    public String depot(Authentication authentication, Model model) {
        BenutzerEntity benutzer = aktuellerBenutzer(authentication);
        DepotEntity depot = benutzer.getDepot();

        Map<BargeldArt, Integer> bestand = new EnumMap<>(BargeldArt.class);

        for (BargeldArt art : BargeldArt.values()) {
            bestand.put(art, 0);
        }

        bestand.putAll(bargeldService.berechneBestand(depot));

        model.addAttribute("depotWert", bargeldService.berechneDepotWert(depot));
        model.addAttribute("bestand", bestand);

        return "depot";
    }

    private BenutzerEntity aktuellerBenutzer(Authentication authentication) {
        return benutzerRepository.findByName(authentication.getName())
                .orElseThrow();
    }
}