package de.julienkowolik.bargeldmanager.entity;

import de.julienkowolik.bargeldmanager.model.BargeldArt;
import de.julienkowolik.bargeldmanager.model.Kategorie;
import de.julienkowolik.bargeldmanager.model.TransaktionsTyp;
import de.julienkowolik.bargeldmanager.service.BargeldService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class BargeldServiceTest {
    private final BargeldService bargeldService = new BargeldService();

    @Test
    @DisplayName("Berechnet den Wert eines Transaktionsbargeldes korrekt")
    void test_1() {
        TransaktionsBargeldEntity transaktionsBargeld = new TransaktionsBargeldEntity(
                BargeldArt.EURO_50,
                2);

        int wert = bargeldService.berechneBargeldWert(transaktionsBargeld);

        assertThat(wert).isEqualTo(100);
    }

    @Test
    @DisplayName("Berechnet den Wert einer Transaktion korrekt")
    void test_2() {
        TransaktionEntity transaktion = new TransaktionEntity();
        transaktion.setTyp(TransaktionsTyp.EINNAHME);
        transaktion.setBargeld(List.of(
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 2),
                new TransaktionsBargeldEntity(BargeldArt.EURO_10, 3)
        ));

        int wert = bargeldService.berechneTransaktionsWert(transaktion);

        assertThat(wert).isEqualTo(130);
    }

    @Test
    @DisplayName("Berechnet den wert eines Depots korrekt")
    void test_3() {
        DepotEntity depot = new DepotEntity();

        TransaktionEntity transaktion = new TransaktionEntity();
        transaktion.setTyp(TransaktionsTyp.EINNAHME);
        transaktion.setBargeld(List.of(
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 2),
                new TransaktionsBargeldEntity(BargeldArt.EURO_10, 3)
        ));

        TransaktionEntity transaktion2 = new TransaktionEntity();
        transaktion2.setTyp(TransaktionsTyp.EINNAHME);
        transaktion2.setBargeld(List.of(
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 3),
                new TransaktionsBargeldEntity(BargeldArt.EURO_20, 3)
        ));

        depot.addTransaktion(transaktion);
        depot.addTransaktion(transaktion2);

        int wert = bargeldService.berechneDepotWert(depot);

        assertThat(wert).isEqualTo(340);
    }

    @Test
    @DisplayName("Berechnet den Bestand eines Depots korrekt")
    void Test_4() {
        DepotEntity depot = new DepotEntity();

        TransaktionEntity einnahme = new TransaktionEntity();
        einnahme.setTyp(TransaktionsTyp.EINNAHME);
        einnahme.setDepot(depot);

        TransaktionsBargeldEntity bargeld1 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 2, einnahme);
        TransaktionsBargeldEntity bargeld2 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_20, 3, einnahme);

        einnahme.setBargeld(List.of(bargeld1, bargeld2));

        TransaktionEntity ausgabe = new TransaktionEntity();

        ausgabe.setTyp(TransaktionsTyp.AUSGABE);
        ausgabe.setDepot(depot);

        TransaktionsBargeldEntity bargeld3 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 1, ausgabe);
        TransaktionsBargeldEntity bargeld4 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_20, 2, ausgabe);

        ausgabe.setBargeld(List.of(bargeld3, bargeld4));

        depot.addTransaktion(einnahme);
        depot.addTransaktion(ausgabe);

        Map<BargeldArt, Integer> bestand =
                bargeldService.berechneBestand(depot);

        assertThat(bestand.get(BargeldArt.EURO_50)).isEqualTo(1);
        assertThat(bestand.get(BargeldArt.EURO_20)).isEqualTo(1);
    }

    @Test
    @DisplayName("Bestimmt ob ein Depot genug Bargeld hat fall true")
    void test_5() {
        DepotEntity depot = new DepotEntity();

        TransaktionEntity einnahme = new TransaktionEntity();
        einnahme.setTyp(TransaktionsTyp.EINNAHME);
        einnahme.setDepot(depot);

        TransaktionsBargeldEntity bargeld1 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 2, einnahme);
        TransaktionsBargeldEntity bargeld2 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_20, 3, einnahme);

        einnahme.setBargeld(List.of(bargeld1, bargeld2));

        TransaktionEntity ausgabe = new TransaktionEntity();

        ausgabe.setTyp(TransaktionsTyp.AUSGABE);
        ausgabe.setDepot(depot);

        TransaktionsBargeldEntity bargeld3 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 1, ausgabe);
        TransaktionsBargeldEntity bargeld4 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_20, 2, ausgabe);

        ausgabe.setBargeld(List.of(bargeld3, bargeld4));

        depot.addTransaktion(einnahme);


        boolean hatGenugBargeld = bargeldService.hatGenugBargeld(depot, ausgabe);

        assertThat(hatGenugBargeld).isTrue();
    }

    @Test
    @DisplayName("Bestimmt ob ein Depot genug Bargeld hat fall false")
    void test_6() {
        DepotEntity depot = new DepotEntity();

        TransaktionEntity einnahme = new TransaktionEntity();
        einnahme.setTyp(TransaktionsTyp.EINNAHME);
        einnahme.setDepot(depot);

        TransaktionsBargeldEntity bargeld1 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 2, einnahme);
        TransaktionsBargeldEntity bargeld2 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_20, 3, einnahme);

        einnahme.setBargeld(List.of(bargeld1, bargeld2));

        TransaktionEntity ausgabe = new TransaktionEntity();

        ausgabe.setTyp(TransaktionsTyp.AUSGABE);
        ausgabe.setDepot(depot);

        TransaktionsBargeldEntity bargeld3 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 1, ausgabe);
        TransaktionsBargeldEntity bargeld4 =
                new TransaktionsBargeldEntity(BargeldArt.EURO_20, 2, ausgabe);

        ausgabe.setBargeld(List.of(bargeld3, bargeld4));

        depot.addTransaktion(einnahme);
        depot.addTransaktion(ausgabe);

        boolean hatGenugBargeld = bargeldService.hatGenugBargeld(depot, ausgabe);

        assertThat(hatGenugBargeld).isFalse();
    }
    private final BargeldService service = new BargeldService();

    @Test
    void berechnetBargeldWert() {
        TransaktionsBargeldEntity bargeld =
                new TransaktionsBargeldEntity(BargeldArt.EURO_50, 2, null);

        int wert = service.berechneBargeldWert(bargeld);

        assertThat(wert).isEqualTo(100);
    }

    @Test
    void berechnetEinnahmeTransaktionsWertPositiv() {
        TransaktionEntity einnahme = transaktion(
                TransaktionsTyp.EINNAHME,
                Kategorie.GEHALT,
                LocalDateTime.of(2026, 6, 7, 12, 0),
                bargeld(BargeldArt.EURO_50, 2),
                bargeld(BargeldArt.EURO_20, 3)
        );

        int wert = service.berechneTransaktionsWert(einnahme);

        assertThat(wert).isEqualTo(160);
    }

    @Test
    void berechnetAusgabeTransaktionsWertNegativ() {
        TransaktionEntity ausgabe = transaktion(
                TransaktionsTyp.AUSGABE,
                Kategorie.GAMING,
                LocalDateTime.of(2026, 6, 7, 12, 0),
                bargeld(BargeldArt.EURO_50, 1),
                bargeld(BargeldArt.EURO_20, 1)
        );

        int wert = service.berechneTransaktionsWert(ausgabe);

        assertThat(wert).isEqualTo(-70);
    }

    @Test
    void berechnetDepotWertAusEinnahmenUndAusgaben() {
        DepotEntity depot = depotMit(
                transaktion(TransaktionsTyp.EINNAHME, Kategorie.GEHALT,
                        LocalDateTime.of(2026, 6, 7, 12, 0),
                        bargeld(BargeldArt.EURO_100, 2)),
                transaktion(TransaktionsTyp.AUSGABE, Kategorie.GAMING,
                        LocalDateTime.of(2026, 6, 7, 13, 0),
                        bargeld(BargeldArt.EURO_50, 1))
        );

        int wert = service.berechneDepotWert(depot);

        assertThat(wert).isEqualTo(150);
    }

    @Test
    void berechnetBestandNachScheinen() {
        DepotEntity depot = depotMit(
                transaktion(TransaktionsTyp.EINNAHME, Kategorie.GEHALT,
                        LocalDateTime.now(),
                        bargeld(BargeldArt.EURO_50, 2),
                        bargeld(BargeldArt.EURO_20, 3)),
                transaktion(TransaktionsTyp.AUSGABE, Kategorie.GAMING,
                        LocalDateTime.now(),
                        bargeld(BargeldArt.EURO_50, 1),
                        bargeld(BargeldArt.EURO_20, 2))
        );

        Map<BargeldArt, Integer> bestand = service.berechneBestand(depot);

        assertThat(bestand.get(BargeldArt.EURO_50)).isEqualTo(1);
        assertThat(bestand.get(BargeldArt.EURO_20)).isEqualTo(1);
    }

    @Test
    void erkenntWennGenugBargeldVorhandenIst() {
        DepotEntity depot = depotMit(
                transaktion(TransaktionsTyp.EINNAHME, Kategorie.GEHALT,
                        LocalDateTime.now(),
                        bargeld(BargeldArt.EURO_50, 2))
        );

        TransaktionEntity ausgabe = transaktion(
                TransaktionsTyp.AUSGABE,
                Kategorie.GAMING,
                LocalDateTime.now(),
                bargeld(BargeldArt.EURO_50, 1)
        );

        assertThat(service.hatGenugBargeld(depot, ausgabe)).isTrue();
    }

    @Test
    void erkenntWennNichtGenugBargeldVorhandenIst() {
        DepotEntity depot = depotMit(
                transaktion(TransaktionsTyp.EINNAHME, Kategorie.GEHALT,
                        LocalDateTime.now(),
                        bargeld(BargeldArt.EURO_50, 1))
        );

        TransaktionEntity ausgabe = transaktion(
                TransaktionsTyp.AUSGABE,
                Kategorie.GAMING,
                LocalDateTime.now(),
                bargeld(BargeldArt.EURO_50, 2)
        );

        assertThat(service.hatGenugBargeld(depot, ausgabe)).isFalse();
    }

    @Test
    void berechnetAusgabenNachKategorie() {
        DepotEntity depot = depotMit(
                transaktion(TransaktionsTyp.AUSGABE, Kategorie.GAMING,
                        LocalDateTime.now(),
                        bargeld(BargeldArt.EURO_50, 1)),
                transaktion(TransaktionsTyp.AUSGABE, Kategorie.GAMING,
                        LocalDateTime.now(),
                        bargeld(BargeldArt.EURO_20, 1)),
                transaktion(TransaktionsTyp.AUSGABE, Kategorie.LEBENSMITTEL,
                        LocalDateTime.now(),
                        bargeld(BargeldArt.EURO_10, 2))
        );

        Map<Kategorie, Integer> ausgaben =
                service.berechneAusgabenNachKategorie(depot);

        assertThat(ausgaben.get(Kategorie.GAMING)).isEqualTo(-70);
        assertThat(ausgaben.get(Kategorie.LEBENSMITTEL)).isEqualTo(-20);
    }

    @Test
    void berechnetMonatsEinnahmenUndAusgaben() {
        DepotEntity depot = depotMit(
                transaktion(TransaktionsTyp.EINNAHME, Kategorie.GEHALT,
                        LocalDateTime.of(2026, 6, 7, 12, 0),
                        bargeld(BargeldArt.EURO_100, 1)),
                transaktion(TransaktionsTyp.AUSGABE, Kategorie.GAMING,
                        LocalDateTime.of(2026, 6, 8, 12, 0),
                        bargeld(BargeldArt.EURO_20, 1)),
                transaktion(TransaktionsTyp.EINNAHME, Kategorie.GEHALT,
                        LocalDateTime.of(2026, 7, 1, 12, 0),
                        bargeld(BargeldArt.EURO_50, 1))
        );

        assertThat(service.berechneMonatsEinnahmen(depot, 2026, Month.JUNE))
                .isEqualTo(100);

        assertThat(service.berechneMonatsAusgaben(depot, 2026, Month.JUNE))
                .isEqualTo(-20);
    }

    private DepotEntity depotMit(TransaktionEntity... transaktionen) {
        DepotEntity depot = new DepotEntity("Testdepot");

        for (TransaktionEntity transaktion : transaktionen) {
            transaktion.setDepot(depot);
            depot.addTransaktion(transaktion);
        }

        return depot;
    }

    private TransaktionEntity transaktion(
            TransaktionsTyp typ,
            Kategorie kategorie,
            LocalDateTime datum,
            TransaktionsBargeldEntity... bargeld
    ) {
        TransaktionEntity transaktion = new TransaktionEntity();
        transaktion.setTyp(typ);
        transaktion.setKathegorie(kategorie);
        transaktion.setDatum(datum);

        List<TransaktionsBargeldEntity> bargeldListe = List.of(bargeld);

        for (TransaktionsBargeldEntity b : bargeldListe) {
            b.setTransaktion(transaktion);
        }

        transaktion.setBargeld(bargeldListe);

        return transaktion;
    }

    private TransaktionsBargeldEntity bargeld(
            BargeldArt art,
            int anzahl
    ) {
        return new TransaktionsBargeldEntity(art, anzahl, null);
    }
}

