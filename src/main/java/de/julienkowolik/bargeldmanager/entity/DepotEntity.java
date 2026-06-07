package de.julienkowolik.bargeldmanager.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DepotEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "depot")
    private List<TransaktionEntity> transaktionen = new ArrayList<>();


    public DepotEntity(String name) {
        this.name = name;
    }

    protected DepotEntity() {
    }

    //Getter
    public Long getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }
    public List<TransaktionEntity> getTransaktionen() {
        return transaktionen;
    }

    //setter
    public void setName(String name) {
        this.name = name;
    }
    public void addTransaktion(TransaktionEntity transaktionen) {
        this.transaktionen.add(transaktionen);
    }

}