package de.julienkowolik.bargeldmanager.entity;

import jakarta.persistence.*;

@Entity
public class BenutzerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String passwortHash;

    @OneToOne(cascade = CascadeType.ALL)
    private DepotEntity depot;

    protected BenutzerEntity() {}

    public BenutzerEntity(String name, String passwort) {
        this.name = name;
        this.passwortHash = passwort;
        this.depot = new DepotEntity("Mein Depot");
    }

    //getter
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPasswortHash() {
        return passwortHash;
    }
    public DepotEntity getDepot() {
        return depot;
    }

    //setter
    public void setName(String name) {
        this.name = name;
    }
    public void setPasswortHash(String passwortHash) {
        this.passwortHash = passwortHash;
    }
    public void setDepot(DepotEntity depo) {
        this.depot = depo;
    }

}
