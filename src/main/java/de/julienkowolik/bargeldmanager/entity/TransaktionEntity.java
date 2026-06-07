package de.julienkowolik.bargeldmanager.entity;

import de.julienkowolik.bargeldmanager.model.Kategorie;
import de.julienkowolik.bargeldmanager.model.TransaktionsTyp;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TransaktionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private TransaktionsTyp typ;

    private LocalDateTime datum;

    @ManyToOne
    @JoinColumn(name = "depot_id")
    private DepotEntity depot;

    private Kategorie kathegorie;

    @OneToMany(mappedBy = "transaktion", cascade = CascadeType.ALL)
    private List<TransaktionsBargeldEntity> bargeld = new ArrayList<>();



    protected TransaktionEntity() {}

    public TransaktionEntity(
            TransaktionsTyp typ,
            DepotEntity depot,
            Kategorie kathegorie,
            List<TransaktionsBargeldEntity> bargeld
    ) {
        this.typ = typ;
        this.datum = LocalDateTime.now();
        this.depot = depot;
        this.kathegorie = kathegorie;
        this.bargeld = bargeld;
    }

    //Getter
    public int getId() {
        return id;
    }
    public TransaktionsTyp getTyp() {
        return typ;
    }
    public LocalDateTime getDatum() {
        return datum;
    }
    public DepotEntity getDepot() {
        return depot;
    }
    public Kategorie getKathegorie() {
        return kathegorie;
    }
    public List<TransaktionsBargeldEntity> getBargeld() {
        return bargeld;
    }

    public int getGesamtwert() {
        return bargeld.stream()
                .mapToInt(b -> b.getArt().getWert() * b.getAnzahl())
                .sum();
    }

    //Setter
    public void setTyp(TransaktionsTyp typ) {
        this.typ = typ;
    }
    public void setDatum(LocalDateTime datum) {
        this.datum = datum;
    }
    public void setDepot(DepotEntity depot) {
        this.depot = depot;
    }
    public void setKathegorie(Kategorie kathegorie) {
        this.kathegorie = kathegorie;
    }
    public void setBargeld(List<TransaktionsBargeldEntity> bargeld) {
        this.bargeld = bargeld;
    }

}