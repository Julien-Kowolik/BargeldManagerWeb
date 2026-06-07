package de.julienkowolik.bargeldmanager.service;

import de.julienkowolik.bargeldmanager.entity.DepotEntity;
import de.julienkowolik.bargeldmanager.entity.TransaktionEntity;
import de.julienkowolik.bargeldmanager.entity.TransaktionsBargeldEntity;
import de.julienkowolik.bargeldmanager.model.BargeldArt;
import de.julienkowolik.bargeldmanager.model.Kategorie;
import de.julienkowolik.bargeldmanager.model.TransaktionsTyp;
import org.springframework.stereotype.Service;

import java.time.Month;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class BargeldService {

    private int vorzeichen(TransaktionEntity transaktion) {
        return transaktion.getTyp() == TransaktionsTyp.EINNAHME
                ? 1
                : -1;
    }

    public int berechneBargeldWert(TransaktionsBargeldEntity bargeld) {
        return bargeld.getArt().getWert() * bargeld.getAnzahl();
    }

    public int berechneTransaktionsWert(TransaktionEntity transaktion) {
        return transaktion.getBargeld()
                .stream()
                .mapToInt(this::berechneBargeldWert)
                .sum() * vorzeichen(transaktion);
    }

    public int berechneDepotWert(DepotEntity depot) {
        return depot.getTransaktionen()
                .stream()
                .mapToInt(this::berechneTransaktionsWert)
                .sum();
    }

    public Map<BargeldArt, Integer> berechneBestand(DepotEntity depot) {
        return depot.getTransaktionen()
                .stream()
                .flatMap(b -> b.getBargeld().stream())
                .collect(Collectors.groupingBy(
                        TransaktionsBargeldEntity::getArt,
                        Collectors.summingInt(
                                b -> b.getAnzahl()
                                        * vorzeichen(b.getTransaktion())))
                );
    }


    public boolean hatGenugBargeld(
            DepotEntity depot,
            TransaktionEntity ausgaben
    ) {
        Map<BargeldArt, Integer> bargeldBestand = berechneBestand(depot);
        return ausgaben.getBargeld()
                .stream()
                .allMatch(ausgabe ->
                         bargeldBestand.getOrDefault(ausgabe.getArt(), 0) >= ausgabe.getAnzahl());
    }

    public int berechneGesamtEinnahmen(DepotEntity depot) {
        return depot.getTransaktionen()
                .stream()
                .filter(t -> t.getTyp() == TransaktionsTyp.EINNAHME)
                .mapToInt(this::berechneTransaktionsWert)
                .sum();
    }

    public int berechneGesamtAusgaben(DepotEntity depot) {
        return depot.getTransaktionen()
                .stream()
                .filter(t -> t.getTyp() == TransaktionsTyp.AUSGABE)
                .mapToInt(this::berechneTransaktionsWert)
                .sum();
    }

    public Map<Kategorie, Integer> berechneAusgabenNachKategorie(DepotEntity depot) {
        return depot.getTransaktionen()
                .stream()
                .filter(t -> t.getTyp() == TransaktionsTyp.AUSGABE)
                .collect(Collectors.groupingBy(
                        TransaktionEntity::getKathegorie,
                        Collectors.summingInt(this::berechneTransaktionsWert)
                ));
    }

    public int berechneMonatsEinnahmen(DepotEntity depot, int jahr, Month monat) {
        return depot.getTransaktionen()
                .stream()
                .filter(t -> t.getTyp() == TransaktionsTyp.EINNAHME)
                .filter(t -> t.getDatum().getYear() == jahr)
                .filter(t -> t.getDatum().getMonth() == monat)
                .mapToInt(this::berechneTransaktionsWert)
                .sum();
    }

    public int berechneMonatsAusgaben(DepotEntity depot, int jahr, Month monat) {
        return depot.getTransaktionen()
                .stream()
                .filter(t -> t.getTyp() == TransaktionsTyp.AUSGABE)
                .filter(t -> t.getDatum().getYear() == jahr)
                .filter(t -> t.getDatum().getMonth() == monat)
                .mapToInt(this::berechneTransaktionsWert)
                .sum();
    }

    public int berechneJahresEinnahmen(DepotEntity depot, int jahr) {
        return depot.getTransaktionen()
                .stream()
                .filter(t -> t.getTyp() == TransaktionsTyp.EINNAHME)
                .filter(t -> t.getDatum().getYear() == jahr)
                .mapToInt(this::berechneTransaktionsWert)
                .sum();
    }

    public int berechneJahresAusgaben(DepotEntity depot, int jahr) {
        return depot.getTransaktionen()
                .stream()
                .filter(t -> t.getTyp() == TransaktionsTyp.AUSGABE)
                .filter(t -> t.getDatum().getYear() == jahr)
                .mapToInt(this::berechneTransaktionsWert)
                .sum();
    }

    public int berechneMonatsSaldo(DepotEntity depot, int jahr, Month monat) {
        return berechneMonatsEinnahmen(depot, jahr, monat) + berechneMonatsAusgaben(depot, jahr, monat);
    }

    public int berechneJahresSaldo(DepotEntity depot, int jahr) {
        return berechneJahresEinnahmen(depot, jahr) + berechneJahresAusgaben(depot, jahr);
    }
}