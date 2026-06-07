package de.julienkowolik.bargeldmanager.model;

public enum BargeldArt {

    EURO_5(5),
    EURO_10(10),
    EURO_20(20),
    EURO_50(50),
    EURO_100(100),
    EURO_200(200);

    private final int wert;

    BargeldArt(int wert) {
        this.wert = wert;
    }

    public int getWert() {
        return wert;
    }
}