package com.scanner.fbs_scanner;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;


// Diese Klasse ermöglicht diverse Dateioperationen
public final class DateiHelper{ //implements ActivityCompat.OnRequestPermissionsResultCallback{


    // prüfe, ob Permission vorhanden, wenn nicht hole diese vom User ein

    // erstelle csv-Datei (wenn noch nicht vorhanden) mit dem Namen des eingegebenen Raums und schreibe hinein

    // lies den Inhalt der jeweiligen Datei aus

    // liefere alle Dateinamen eines Verzeichnisses und returne sie in Form eines Stringarrays

    // eigener Thread?

    // bevor Berechtigungen abgefragt werden, dieser Variable die anfragende Activityklasse zuweisen
    // (DateiHelper.activityPlaceholder = Hauptmenue.this;)
    public static Activity activityPlaceholder;

    private static boolean externerSpeicherLesbar, externerSpeicherBeschreibbar;
    public static final String schreibPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String lesePermission= Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final int REQUEST_CODE_WRITE = 100;
    public static final int REQUEST_CODE_READ = 200;


    // prüft, ob hardwareseitig Speicher zum Lesen und Schreiben verfügbar ist und setzt entsprechend die Variablen
    // diese Methode sollte aufgerufen werden, bevor eine Datei erstellt/beschrieben oder ausgelesen wird
    public static void pruefeSpeicher()
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

    // prüft, ob Leseberechtigungen vorliegen und fordert diese wenn nötig an
    public static void fordereLesePermissionAn() {
        // ist API-Version kleiner 23, werden Permissions schon bei der Installation zugestimmt
        if(Build.VERSION.SDK_INT < 23) {
            return;
        }
        else {
            // wenn die Permission vorliegt, mache nichts
            if(ContextCompat.checkSelfPermission(activityPlaceholder, lesePermission) == PackageManager.PERMISSION_GRANTED) {
                return;
            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(activityPlaceholder, lesePermission)) {
                    new AlertDialog.Builder( activityPlaceholder )
                            .setTitle("Leseberechtigung erforderlich")
                            .setMessage("Zum Abrufen diverser Dateien wird eine Leseberechtigung benötigt." )
                            .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions( activityPlaceholder, new String[]{lesePermission}, REQUEST_CODE_READ );
                                }
                            } )
                            .setNegativeButton( "Abbrechen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            } )
                            .create().show();
                }
                else {
                    ActivityCompat.requestPermissions(activityPlaceholder, new String[]{lesePermission}, REQUEST_CODE_READ );
                }
            }
        }
    }

    // prüft, ob Schreibberechtigungen vorliegen und fordert diese wenn nötig an
    public static void fordereSchreibPermissionAn() {
        // ist API-Version kleiner 23, werden Permissions schon bei der Installation zugestimmt
        if(Build.VERSION.SDK_INT < 23) {
            return;
        }
        else {
            // wenn die Permission vorliegt, mache nichts
            if(ContextCompat.checkSelfPermission(activityPlaceholder, schreibPermission) == PackageManager.PERMISSION_GRANTED) {
                return;
            }
            else{
                if(ActivityCompat.shouldShowRequestPermissionRationale(activityPlaceholder, schreibPermission)) {
                    new AlertDialog.Builder( activityPlaceholder )
                            .setTitle("Schreibberechtigung erforderlich")
                            .setMessage("Zum Abspeichern der erfassten Daten in Dateien wird eine Schreibberechtigung benötigt." )
                            .setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ActivityCompat.requestPermissions( activityPlaceholder, new String[]{schreibPermission}, REQUEST_CODE_WRITE );
                                }
                            } )
                            .setNegativeButton( "Abbrechen", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            } )
                            .create().show();
                }
                else {
                    ActivityCompat.requestPermissions(activityPlaceholder, new String[]{schreibPermission}, REQUEST_CODE_WRITE );
                }
            }
        }
    }

    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults ){

        if(requestCode == REQUEST_CODE_READ){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(contextPlaceholder, "Lesezugriff erteilt.",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(contextPlaceholder, "Lesezugriff verweigert.",Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == REQUEST_CODE_WRITE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(contextPlaceholder, "Schreibzugriff erteilt.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(contextPlaceholder, "Lesezugriff verweigert.",Toast.LENGTH_SHORT).show();
            }
        }
    }*/
}