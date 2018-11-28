package com.scanner.fbs_scanner.Standardklassen;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.scanner.fbs_scanner.Activityklassen.Scannen;
import com.scanner.fbs_scanner.R;

import java.util.ArrayList;

public class TinyDBHelper {

    public static Activity activityPlaceholder;
    private static TinyDB tinyDB = new TinyDB(activityPlaceholder);
    public static ArrayList<String> geraettypen = new ArrayList<>(  );

    //Gebe alle Typen zurück
    public static ArrayList<String>  gebeTypen(){
        return tinyDB.getListString("KEY_TYP");
    }

    //Füge Typ hinzu
    public void addGeraettyp(String geraettyp){
        geraettypen = tinyDB.getListString("KEY_TYP");
        geraettypen.add(geraettyp);
        speicherTypen(geraettypen);
    }

    //Lösche Typ
    public static void loescheGeraettyp(int arraynumber){
        geraettypen = tinyDB.getListString("KEY_TYP");
        geraettypen.remove(arraynumber);
        speicherTypen(geraettypen);
    }

    public static void ersterStart(){
        if (tinyDB.gebeStandardBoolean("KEY_ErsterStart",true)){
         /*   geraettypen.add(0,App.getContext().getResources().getString(R.string.pc));
            geraettypen.add(1,App.getContext().getResources().getString(R.string.monitor));
            geraettypen.add(2,App.getContext().getResources().getString(R.string.drucker));
            geraettypen.add(3,App.getContext().getResources().getString(R.string.beamer));
            speicherTypen(geraettypen);
            tinyDB.setzeStandardBoolean("KEY_ErsterStart",false);
        */}
    }

    public static void speicherTypen(ArrayList<String> typ){
        tinyDB.putListString("KEY_TYP", typ);
    }
}
