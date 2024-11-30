package org.beadando.beadando;

public class CombinedRecord {

    private String datum;
    private String nev;
    private String helyszin;
    private int pilotaaz;
    private int helyezes;
    private String hiba;
    private String csapat;
    private String tipus;
    private String motor;

    // Pilóta adatok
    private String pilotaNev;
    private String pilotaNem;
    private String pilotaSzuldat;
    private String pilotaNemzet;

    public CombinedRecord(String datum, String nev, String helyszin, int pilotaaz, int helyezes, String hiba, String csapat,
                          String tipus, String motor, String pilotaNev, String pilotaNem, String pilotaSzuldat, String pilotaNemzet) {
        this.datum = datum;
        this.nev = nev;
        this.helyszin = helyszin;
        this.pilotaaz = pilotaaz;
        this.helyezes = helyezes;
        this.hiba = hiba;
        this.csapat = csapat;
        this.tipus = tipus;
        this.motor = motor;

        this.pilotaNev = pilotaNev;
        this.pilotaNem = pilotaNem;
        this.pilotaSzuldat = pilotaSzuldat;
        this.pilotaNemzet = pilotaNemzet;
    }

    // Getterek és Setterek
    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getNev() {
        return nev;
    }

    public void setNev(String nev) {
        this.nev = nev;
    }

    public String getHelyszin() {
        return helyszin;
    }

    public void setHelyszin(String helyszin) {
        this.helyszin = helyszin;
    }

    public int getPilotaaz() {
        return pilotaaz;
    }

    public void setPilotaaz(int pilotaaz) {
        this.pilotaaz = pilotaaz;
    }

    public int getHelyezes() {
        return helyezes;
    }

    public void setHelyezes(int helyezes) {
        this.helyezes = helyezes;
    }

    public String getHiba() {
        return hiba;
    }

    public void setHiba(String hiba) {
        this.hiba = hiba;
    }

    public String getCsapat() {
        return csapat;
    }

    public void setCsapat(String csapat) {
        this.csapat = csapat;
    }

    public String getTipus() {
        return tipus;
    }

    public void setTipus(String tipus) {
        this.tipus = tipus;
    }

    public String getMotor() {
        return motor;
    }

    public void setMotor(String motor) {
        this.motor = motor;
    }

    // Pilóta adatok getterek
    public String getPilotaNev() {
        return pilotaNev;
    }

    public void setPilotaNev(String pilotaNev) {
        this.pilotaNev = pilotaNev;
    }

    public String getPilotaNem() {
        return pilotaNem;
    }

    public void setPilotaNem(String pilotaNem) {
        this.pilotaNem = pilotaNem;
    }

    public String getPilotaSzuldat() {
        return pilotaSzuldat;
    }

    public void setPilotaSzuldat(String pilotaSzuldat) {
        this.pilotaSzuldat = pilotaSzuldat;
    }

    public String getPilotaNemzet() {
        return pilotaNemzet;
    }

    public void setPilotaNemzet(String pilotaNemzet) {
        this.pilotaNemzet = pilotaNemzet;
    }
}
