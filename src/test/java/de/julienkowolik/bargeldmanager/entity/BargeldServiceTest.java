package de.julienkowolik.bargeldmanager.entity;

import de.julienkowolik.bargeldmanager.model.BargeldArt;
import de.julienkowolik.bargeldmanager.model.TransaktionsTyp;
import de.julienkowolik.bargeldmanager.service.BargeldService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}

