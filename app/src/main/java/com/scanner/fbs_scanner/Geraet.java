package com.scanner.fbs_scanner;

import java.util.ArrayList;

// Jede Instanz dieser Klasse enthält die Informationen für ein Gerät
public class Geraet {

    private String raumNr;
    private String geraeteTyp;
    private String inventarnummer;
    private String notiz;
    public static ArrayList<Geraet> geraeteliste = new ArrayList<>(  );

    public Geraet(String raumNr, String geraeteTyp, String inventarnummer, String notiz)
    {
        this.raumNr = raumNr;
        this.geraeteTyp = geraeteTyp;
        this.inventarnummer = inventarnummer;
        this.notiz = notiz;
    }

    public String getRaumNr() {
        return raumNr;
    }

    public String getGeraeteTyp() {
        return geraeteTyp;
    }

    public String getInventarnummer() {
        return inventarnummer;
    }

    public String getNotiz() {
        return notiz;
    }

    public void setRaumNr(String raumNr) {
        this.raumNr = raumNr;
    }

    public void setGeraeteTyp(String geraeteTyp) {
        this.geraeteTyp = geraeteTyp;
    }

    public void setInventarnummer(String inventarnummer) {
        this.inventarnummer = inventarnummer;
    }

    public void setNotiz(String notiz) {
        this.notiz = notiz;
    }
}
