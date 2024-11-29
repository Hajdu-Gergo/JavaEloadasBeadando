package org.beadando.beadando;

public class Pilota {
    private String nev;
    private String nem;
    private String szuldat;
    private String nemzet;

    public Pilota(String nev, String nem, String szuldat, String nemzet) {
        this.nev = nev;
        this.nem = nem;
        this.szuldat = szuldat;
        this.nemzet = nemzet;
    }

    public String getNev() {
        return nev;
    }

    public String getNem() {
        return nem;
    }

    public String getSzuldat() {
        return szuldat;
    }

    public String getNemzet() {
        return nemzet;
    }
}
