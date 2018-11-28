package com.scanner.fbs_scanner.Standardklassen;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;
import com.scanner.fbs_scanner.Activityklassen.Hauptmenue;
import com.scanner.fbs_scanner.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

// Diese Klasse ermöglicht diverse Dateioperationen
public final class DateiHelper{

    // bevor Berechtigungen abgefragt werden, dieser Variable die anfragende Activityklasse zuweisen
    // (z.B. DateiHelper.activityPlaceholder = Hauptmenue.this;)
    public static Activity activityPlaceholder;

    private static boolean externerSpeicherLesbar, externerSpeicherBeschreibbar;
    public static final String schreibPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String lesePermission= Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int STORAGE_REQUEST_CODE = 100;
    private static File rootVerzeichnis = Environment.getExternalStorageDirectory();
    private static File csvVerzeichnis = new File(rootVerzeichnis.getAbsolutePath(),"/FBS");


    // prüft, ob hardwareseitig Speicher zum Lesen und Schreiben verfügbar ist und setzt entsprechend die Variablen
    // diese Methode sollte aufgerufen werden, bevor eine Datei erstellt/beschrieben oder ausgelesen wird
    private static void pruefeSpeicher()
    {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            externerSpeicherLesbar = externerSpeicherBeschreibbar = true;
        } else if (state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
            externerSpeicherLesbar = true;
            externerSpeicherBeschreibbar = false;
        } else {
            externerSpeicherLesbar = externerSpeicherBeschreibbar = false;
        }
    }

    // prüft, ob Lese- und Schreibberechtigungen vorliegen und fordert diese wenn nötig an
    // liegen die Berechtigungn schon vor, wird bereits in dieser Methode die Datei erstellt
    public static void fordereLeseUndSchreibPermissionAn() {
        // ist API-Version kleiner 23, werden Permissions schon bei der Installation zugestimmt
        if(Build.VERSION.SDK_INT < 23) {
            return;
        }
        else {
            if(ContextCompat.checkSelfPermission(activityPlaceholder, lesePermission) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(activityPlaceholder, schreibPermission) != PackageManager.PERMISSION_GRANTED) {

                if(ActivityCompat.shouldShowRequestPermissionRationale(activityPlaceholder, schreibPermission)) {
                    new AlertDialog.Builder( activityPlaceholder )
                            .setTitle(App.getContext().getResources().getString(R.string.dateihelper_berechtigung))
                            .setMessage(App.getContext().getResources().getString(R.string.dateihelper_berechtigung_message) )
                            .setPositiveButton(App.getContext().getResources().getString(R.string.string_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions( activityPlaceholder, new String[]{lesePermission,schreibPermission}, STORAGE_REQUEST_CODE);
                                }
                            } )
                            .setNegativeButton( App.getContext().getResources().getString(R.string.string_abbrechen), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            } )
                            .create().show();
                }
                else {
                    ActivityCompat.requestPermissions(activityPlaceholder, new String[]{lesePermission, schreibPermission}, STORAGE_REQUEST_CODE );
                }
            }
            // sind die Berechtigungen schon vorhanden, springt das Programm nicht in die onRequestPermissionsResult
            // Methode, deswegen müssen an dieser Stelle die sonst üblichen Operationen durchgeführt werden!
            else {
                erzeugeUndBeschreibeDatei();
                Toast.makeText(activityPlaceholder, App.getContext().getResources().getString(R.string.scan_dateierstellt),Toast.LENGTH_LONG).show();
                Geraet.geraeteliste.clear();
                activityPlaceholder.startActivity(new Intent(activityPlaceholder, Hauptmenue.class));
            }
        }
    }

    // erzeugt eine Datei, dessen Name = jeweiliger Raumname einer Erfassung
    // beschreibt die Datei mit den hinzugefügten Geräten
    public static void erzeugeUndBeschreibeDatei(){
        if(Geraet.geraeteliste.get(0) != null) {
            String dateiRaumName = Geraet.geraeteliste.get( 0 ).getRaumName().trim().replace( ",","." ) + ".csv";

            pruefeSpeicher();
            if (externerSpeicherBeschreibbar) {
                if (!csvVerzeichnis.exists()) {
                    csvVerzeichnis.mkdir();

                    // stelle sicher, dass der Zugriff auf die erstellte Datei möglich ist
                    // und sich das Filesystem aktualisiert
                    csvVerzeichnis.setReadable( true );
                    csvVerzeichnis.setWritable( true );
                    csvVerzeichnis.setExecutable( true );
                    MediaScannerConnection.scanFile(activityPlaceholder, new String[] {csvVerzeichnis.toString()}, null, null);
                }
                File datei = new File( csvVerzeichnis, dateiRaumName );
                try {
                    FileOutputStream fos = new FileOutputStream( datei, false);
                    for (Geraet geraet : Geraet.geraeteliste) {
                        fos.write( geraet.gibDateiFormat().getBytes() );
                    }
                    fos.flush();
                    fos.close();

                    datei.setReadable( true );
                    datei.setWritable( true );
                    datei.setExecutable( true );
                    MediaScannerConnection.scanFile( activityPlaceholder, new String[]{datei.getPath()}, null,null );
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // gleicht den übergebenen Raumnamen (ohne .csv) mit den im Verzeichnis FBS befindlichen csv-Dateien
    // ab, bei Übereinstimmung wird die jeweilige Datei ausgelesen und in Form eines Geraete-Arrays zurückgegeben
    public static ArrayList<Geraet> leseDateiAus(String raumname)
    {
        ArrayList<Geraet> listeErfassungen = new ArrayList<>();

        for(File f : csvVerzeichnis.listFiles()){
            if(f.getName().substring(0,f.getName().length() -4).equals(raumname)){
                File file = new File(csvVerzeichnis,f.getName());
                try {
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    String zeile;

                    while((zeile = br.readLine()) != null) {
                        String[] temp = zeile.split( ";" );
                        Geraet erfassung = new Geraet( temp[0], temp[1], temp[2], temp[3] );
                        listeErfassungen.add( erfassung );
                    }
                    br.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return listeErfassungen;
    }

    // gibt alle im Verzeichnis FBS befindlichen csv-Dateien ohne Suffix in Form eines String-Arrays zurück
    public static ArrayList<String> gibRaumListe() {
        ArrayList<String> raumliste = new ArrayList<>();

        for(File f : csvVerzeichnis.listFiles()) {
            if(f.isFile() && f.getName().endsWith( ".csv" )) {
                raumliste.add(f.getName().substring(0,f.getName().length() -4));
            }
            //Collections.sort(raumliste); // Todo: Raumsortierung


        }
        return sortiereListe(raumliste);
    }

    // löscht die Datei, die als Parameter in Form des reinen Raumanmens übergeben wird
    public static void loescheDatei(String raumname, Activity activity){
        boolean geloescht = false;
        for(File f : csvVerzeichnis.listFiles()){
            if(f.getName().substring(0,f.getName().length() -4).equals(raumname)){
                geloescht = f.delete();
            }
            if(geloescht){
                String message = App.getContext().getResources().getString(R.string.dateihelper_msg_raumlöschen_1) + " " + raumname + " " + App.getContext().getResources().getString(R.string.dateihelper_msg_raumlöschen_2);
                Toast.makeText( activity, message,Toast.LENGTH_LONG ).show();
            }
       }
    }

    // Todo: Räume die mit einem Buchstaben beginnen: alphabetisch ordnen und an Nummern appenden
    //       Räume die nach der Nummer einen Buchstaben haben sollen auch sortiert werden, z.B. 3a vor 3b
    // Methode zum Sortieren einer Raumnummern enthaltenden ArrayList vom Typ String
    private static ArrayList<String> sortiereListe(ArrayList<String> unsortierteListe){
        ArrayList<String> sortierteListe = new ArrayList<>();
        double[] arrNummern = new double[unsortierteListe.size()];
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        df.setMinimumFractionDigits(0);
        for(int i = 0; i < unsortierteListe.size(); i++){
            arrNummern[i] = Double.parseDouble(unsortierteListe.get(i));
        }
        Arrays.sort(arrNummern);
        for(int i = 0; i < arrNummern.length; i++){
            sortierteListe.add(String.valueOf(df.format(arrNummern[i])).replace(",","."));
        }
        return sortierteListe;
    }
}