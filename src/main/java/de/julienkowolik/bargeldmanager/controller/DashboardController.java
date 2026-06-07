package de.julienkowolik.bargeldmanager.controller;

import de.julienkowolik.bargeldmanager.entity.BenutzerEntity;
import de.julienkowolik.bargeldmanager.entity.DepotEntity;
import de.julienkowolik.bargeldmanager.entity.TransaktionEntity;
import de.julienkowolik.bargeldmanager.model.BargeldArt;
import de.julienkowolik.bargeldmanager.repository.BenutzerRepository;
import de.julienkowolik.bargeldmanager.service.BargeldService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    private final BenutzerRepository benutzerRepository;
    private final BargeldService bargeldService;

    public DashboardController(
            BenutzerRepository benutzerRepository,
            BargeldService bargeldService
    ) {
        this.benutzerRepository = benutzerRepository;
        this.bargeldService = bargeldService;
    }

    @GetMapping("/")
    public String home(Authentication authentication, Model model) {
        BenutzerEntity benutzer = aktuellerBenutzer(authentication);
        DepotEntity depot = benutzer.getDepot();

        Map<BargeldArt, Integer> bestand = new EnumMap<>(BargeldArt.class);
        for (BargeldArt art : BargeldArt.values()) {
            bestand.put(art, 0);
        }
        bestand.putAll(bargeldService.berechneBestand(depot));

        List<TransaktionEntity> letzteTransaktionen = depot.getTransaktionen()
                .stream()
                .sorted(Comparator.comparing(TransaktionEntity::getDatum).reversed())
                .limit(4)
                .toList();

        Map<Object, Integer> werte = new HashMap<>();
        for (TransaktionEntity transaktion : letzteTransaktionen) {
            werte.put(transaktion.getId(), bargeldService.berechneTransaktionsWert(transaktion));
        }

        model.addAttribute("benutzer", benutzer);
        model.addAttribute("depotWert", bargeldService.berechneDepotWert(depot));
        model.addAttribute("gesamtEinnahmen", bargeldService.berechneGesamtEinnahmen(depot));
        model.addAttribute("gesamtAusgaben", bargeldService.berechneGesamtAusgaben(depot));
        model.addAttribute("bestand", bestand);
        model.addAttribute("letzteTransaktionen", letzteTransaktionen);
        model.addAttribute("werte", werte);

        return "home";
    }

    private BenutzerEntity aktuellerBenutzer(Authentication authentication) {
        return benutzerRepository.findByName(authentication.getName())
                .orElseThrow();
    }
}