package org.beadando.beadando;

public class GP {
    private String datum;
    private String nev;
    private String helyszin;

    public GP(String datum, String nev, String helyszin) {
        this.datum = datum;
        this.nev = nev;
        this.helyszin = helyszin;
    }

    public String getDatum() {
        return datum;
    }

    public String getNev() {
        return nev;
    }

    public String getHelyszin() {
        return helyszin;
    }
}