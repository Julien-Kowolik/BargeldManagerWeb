package de.julienkowolik.bargeldmanager.entity;

import jakarta.persistence.*;

@Entity
public class BenutzerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String passwort;

    @OneToOne
    private DepotEntity depot;

    protected BenutzerEntity() {}

    public BenutzerEntity(String name, String passwort) {
        this.name = name;
        this.passwort = passwort;
        this.depot = new DepotEntity("Mein Depot");
    }

    //getter
    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getPasswort() {
        return passwort;
    }
    public DepotEntity getDepot() {
        return depot;
    }

    //setter
    public void setName(String name) {
        this.name = name;
    }
    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }
    public void setDepot(DepotEntity depo) {
        this.depot = depo;
    }

}
