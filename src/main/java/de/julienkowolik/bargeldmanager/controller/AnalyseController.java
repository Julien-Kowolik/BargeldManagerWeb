package de.julienkowolik.bargeldmanager.controller;

import de.julienkowolik.bargeldmanager.entity.BenutzerEntity;
import de.julienkowolik.bargeldmanager.entity.DepotEntity;
import de.julienkowolik.bargeldmanager.repository.BenutzerRepository;
import de.julienkowolik.bargeldmanager.service.BargeldService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AnalyseController {

    private final BenutzerRepository benutzerRepository;
    private final BargeldService bargeldService;

    public AnalyseController(BenutzerRepository benutzerRepository,
                             BargeldService bargeldService) {
        this.benutzerRepository = benutzerRepository;
        this.bargeldService = bargeldService;
    }

    @GetMapping("/analyse")
    public String analyse(Authentication authentication, Model model) {
        BenutzerEntity benutzer = aktuellerBenutzer(authentication);
        DepotEntity depot = benutzer.getDepot();

        model.addAttribute("gesamtEinnahmen", bargeldService.berechneGesamtEinnahmen(depot));
        model.addAttribute("gesamtAusgaben", bargeldService.berechneGesamtAusgaben(depot));
        model.addAttribute("depotWert", bargeldService.berechneDepotWert(depot));
        model.addAttribute("ausgabenNachKategorie", bargeldService.berechneAusgabenNachKategorie(depot));

        return "analyse";
    }

    private BenutzerEntity aktuellerBenutzer(Authentication authentication) {
        return benutzerRepository.findByName(authentication.getName())
                .orElseThrow();
    }
}