package org.beadando.beadando;

public class Eredmeny {
    private String datum;
    private int pilotaaz;
    private int helyezes;
    private String hiba;   // Új mező
    private String csapat; // Új mező
    private String tipus;  // Új mező
    private String motor;  // Új mező

    // Konstruktor
    public Eredmeny(String datum, int pilotaaz, int helyezes, String hiba, String csapat, String tipus, String motor) {
        this.datum = datum;
        this.pilotaaz = pilotaaz;
        this.helyezes = helyezes;
        this.hiba = hiba;
        this.csapat = csapat;
        this.tipus = tipus;
        this.motor = motor;
    }

    // Getterek
    public String getDatum() {
        return datum;
    }

    public int getPilotaaz() {
        return pilotaaz;
    }

    public int getHelyezes() {
        return helyezes;
    }

    public String getHiba() {
        return hiba;
    }

    public String getCsapat() {
        return csapat;
    }

    public String getTipus() {
        return tipus;
    }

    public String getMotor() {
        return motor;
    }
}