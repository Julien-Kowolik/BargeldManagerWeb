package de.julienkowolik.bargeldmanager.controller;

import de.julienkowolik.bargeldmanager.entity.BenutzerEntity;
import de.julienkowolik.bargeldmanager.entity.DepotEntity;
import de.julienkowolik.bargeldmanager.entity.TransaktionEntity;
import de.julienkowolik.bargeldmanager.entity.TransaktionsBargeldEntity;
import de.julienkowolik.bargeldmanager.model.BargeldArt;
import de.julienkowolik.bargeldmanager.model.Kategorie;
import de.julienkowolik.bargeldmanager.model.TransaktionsTyp;
import de.julienkowolik.bargeldmanager.repository.BenutzerRepository;
import de.julienkowolik.bargeldmanager.repository.TransaktionRepository;
import de.julienkowolik.bargeldmanager.service.BargeldService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class TransaktionController {

    private final BenutzerRepository benutzerRepository;
    private final TransaktionRepository transaktionRepository;
    private final BargeldService bargeldService;

    public TransaktionController(
            BenutzerRepository benutzerRepository,
            TransaktionRepository transaktionRepository,
            BargeldService bargeldService
    ) {
        this.benutzerRepository = benutzerRepository;
        this.transaktionRepository = transaktionRepository;
        this.bargeldService = bargeldService;
    }

    @GetMapping("/transaktion/neu")
    public String neueTransaktion(Model model) {
        model.addAttribute("typen", TransaktionsTyp.values());
        model.addAttribute("kategorien", Kategorie.values());

        return "transaktion-form";
    }

    @PostMapping("/transaktion/neu")
    public String transaktionSpeichern(
            Authentication authentication,
            @RequestParam TransaktionsTyp typ,
            @RequestParam Kategorie kategorie,
            @RequestParam int euro5,
            @RequestParam int euro10,
            @RequestParam int euro20,
            @RequestParam int euro50,
            @RequestParam int euro100,
            @RequestParam int euro200
    ) {
        BenutzerEntity benutzer = aktuellerBenutzer(authentication);
        DepotEntity depot = benutzer.getDepot();

        List<TransaktionsBargeldEntity> bargeldListe = new ArrayList<>();

        TransaktionEntity transaktion = new TransaktionEntity(
                typ,
                depot,
                kategorie,
                bargeldListe
        );

        fuegeBargeldHinzu(bargeldListe, transaktion, BargeldArt.EURO_5, euro5);
        fuegeBargeldHinzu(bargeldListe, transaktion, BargeldArt.EURO_10, euro10);
        fuegeBargeldHinzu(bargeldListe, transaktion, BargeldArt.EURO_20, euro20);
        fuegeBargeldHinzu(bargeldListe, transaktion, BargeldArt.EURO_50, euro50);
        fuegeBargeldHinzu(bargeldListe, transaktion, BargeldArt.EURO_100, euro100);
        fuegeBargeldHinzu(bargeldListe, transaktion, BargeldArt.EURO_200, euro200);

        if (bargeldListe.isEmpty()) {
            return "redirect:/transaktion/neu?leer";
        }

        if (typ == TransaktionsTyp.AUSGABE
                && !bargeldService.hatGenugBargeld(depot, transaktion)) {
            return "redirect:/transaktion/neu?nichtGenugBargeld";
        }

        transaktionRepository.save(transaktion);

        return "redirect:/";
    }

    @GetMapping("/transaktionen")
    public String transaktionen(Authentication authentication, Model model) {
        BenutzerEntity benutzer = aktuellerBenutzer(authentication);
        DepotEntity depot = benutzer.getDepot();

        List<TransaktionEntity> transaktionen = depot.getTransaktionen();

        Map<Integer, Integer> werte = new HashMap<>();

        for (TransaktionEntity transaktion : transaktionen) {
            werte.put(
                    transaktion.getId(),
                    bargeldService.berechneTransaktionsWert(transaktion)
            );
        }

        model.addAttribute("transaktionen", transaktionen);
        model.addAttribute("werte", werte);

        return "transaktionen";
    }

    private void fuegeBargeldHinzu(
            List<TransaktionsBargeldEntity> liste,
            TransaktionEntity transaktion,
            BargeldArt art,
            int anzahl
    ) {
        if (anzahl > 0) {
            liste.add(new TransaktionsBargeldEntity(
                    art,
                    anzahl,
                    transaktion
            ));
        }
    }

    private BenutzerEntity aktuellerBenutzer(Authentication authentication) {
        return benutzerRepository.findByName(authentication.getName())
                .orElseThrow();
    }
}