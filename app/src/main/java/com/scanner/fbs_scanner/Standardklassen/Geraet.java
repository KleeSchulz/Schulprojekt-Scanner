package com.scanner.fbs_scanner.Standardklassen;

import java.util.ArrayList;

// Jede Instanz dieser Klasse enthält die Informationen für ein Gerät
public class Geraet {

    private String raumName;
    private String geraeteTyp;
    private String inventarnummer;
    private String notiz;
    public static ArrayList<Geraet> geraeteliste = new ArrayList<>(  );

    public Geraet(String raumName, String geraeteTyp, String inventarnummer, String notiz)
    {
        this.raumName = raumName;
        this.geraeteTyp = geraeteTyp;
        this.inventarnummer = inventarnummer;
        this.notiz = notiz;
    }

    public String gibDateiFormat(){
        return this.raumName.trim().replace(";",",") + ";" + this.geraeteTyp +
                ";" + this.inventarnummer.trim().replace(";",",") +
                ";" + this.notiz.trim().replace(";",",") + "\n";
    }

    public String getRaumName() {
        return raumName;
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

    public void setRaumNr(String raumName) {
        this.raumName = raumName;
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
