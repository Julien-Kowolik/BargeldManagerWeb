package de.julienkowolik.bargeldmanager.entity;

import de.julienkowolik.bargeldmanager.model.BargeldArt;
import jakarta.persistence.*;

@Entity
public class TransaktionsBargeldEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private BargeldArt art;

    private int anzahl;

    @ManyToOne
    @JoinColumn(name = "transaktion_id")
    private TransaktionEntity transaktion;

    protected TransaktionsBargeldEntity() {
    }

    public TransaktionsBargeldEntity(BargeldArt art, int anzahl, TransaktionEntity transaktion) {
        this.transaktion = transaktion;
        this.art = art;
        this.anzahl = anzahl;
    }

    //Getter
    public BargeldArt getArt() {
        return art;
    }

    public int getAnzahl() {
        return anzahl;
    }

    //Setter
    public void setArt(BargeldArt art) {
        this.art = art;
    }

    public void setAnzahl(int anzahl) {
        this.anzahl = anzahl;
    }

    public TransaktionsBargeldEntity(BargeldArt art, int anzahl) {
        this.art = art;
        this.anzahl = anzahl;
    }

    public TransaktionEntity getTransaktion() {
        return transaktion;
    }
    public void setTransaktion(TransaktionEntity transaktion) {
        this.transaktion = transaktion;
    }

}

