package de.julienkowolik.bargeldmanager.model;

public enum Kategorie {

    LEBENSMITTEL("Lebensmittel"),
    RESTAURANT("Restaurant"),
    FAST_FOOD("Fast Food"),
    GETRAENKE("Getränke"),

    MIETE("Miete"),
    STROM("Strom"),
    WASSER("Wasser"),
    INTERNET("Internet"),
    HANDY("Handy"),

    AUTO("Auto"),
    TANKEN("Tanken"),
    PARKEN("Parken"),
    OEPNV("ÖPNV"),

    KLEIDUNG("Kleidung"),
    SCHUHE("Schuhe"),

    FITNESS("Fitness"),
    SPORT("Sport"),

    STUDIUM("Studium"),
    BUECHER("Bücher"),
    SOFTWARE("Software"),

    GAMING("Gaming"),
    STREAMING("Streaming"),
    FILME("Filme"),
    MUSIK("Musik"),

    URLAUB("Urlaub"),
    REISEN("Reisen"),
    HOTEL("Hotel"),

    GESCHENKE("Geschenke"),
    SPENDEN("Spenden"),

    HAUSTIER("Haustier"),

    GEHALT("Gehalt"),
    NEBENJOB("Nebenjob"),
    TASCHENGELD("Taschengeld"),
    GESCHENK_ERHALTEN("Geschenk erhalten"),

    SPAREN("Sparen"),
    INVESTITION("Investition"),

    SONSTIGES("Sonstiges");

    private final String anzeigeName;

    Kategorie(String anzeigeName) {
        this.anzeigeName = anzeigeName;
    }

    public String getAnzeigeName() {
        return anzeigeName;
    }
}