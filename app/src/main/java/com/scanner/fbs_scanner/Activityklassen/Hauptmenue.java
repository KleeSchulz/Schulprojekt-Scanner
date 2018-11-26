package com.scanner.fbs_scanner.Activityklassen;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.scanner.fbs_scanner.Standardklassen.DateiHelper;
import com.scanner.fbs_scanner.R;
import com.scanner.fbs_scanner.Standardklassen.Geraet;
import com.scanner.fbs_scanner.Standardklassen.TinyDB;

import java.util.ArrayList;

public class Hauptmenue extends AppCompatActivity {

    //Todo: Appicon Titlebar zuschneiden und rechts
    //Todo: Responsive Layout für alle Handys! Sebastian                1
    //Todo: Logo Hauptmenü evtl. Hauptmenühintergrund weiß, AppIcon Sebastian      1
    //Todo: Alle Funktionen Testen Bugs beseitigen Sebastian            1
    //Todo: APK-Datei erzeugen! Sebastian                               1
    //Todo: ggf. Toastmessages generell durch Snackbars ersetzten (Snackbars optisch anpassen) Christian
    //Todo: Titelbar Zürückbutton Christian
    //Todo: 2 sprachige App (Englisch u. Deutsch)
    //Todo: Strings.xml - alle hardkodierten Strings einfügen! // Hauptmenüklasse, Raumdetailsklasse, Anzeigeklasse, Scannklasse fertig!
    //todo Logfiles Aktionen des Useres und Fehlermeldungen (ggf. als asynchronen Service implementieren) Christian!

    //todo: verschiedene ActivityStates beachten (z.B. onResume, onRestart, onPause etc. ) Christian
    //todo: bei Klicken auf FBS-Logo wird die FBS-Homepage angezeigt



    Button btn_raumerfassen;
    Button btn_anzeigen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hauptmenue);


        //Setzte Format
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Zuweisungen
        btn_raumerfassen = findViewById(R.id.btn_erfassen);
        btn_anzeigen = findViewById(R.id.btn_anzeigen);


        /* bei Drücken des Buttons btn_raumerfassen wird ein Eingabefeld für den Raumnamen
           angezeigt und dessen Inhalt validiert */
        btn_raumerfassen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = Hauptmenue.this;
                final EditText taskEditText = new EditText(c);
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle(getResources().getString(R.string.hauptstring_raumerfassen));
                builder.setMessage(getResources().getString(R.string.hauptstring_raumnameeingabe));
                builder.setView(taskEditText);
                builder.setPositiveButton(getResources().getString(R.string.hauptstring_raumscannen), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String raum = String.valueOf(taskEditText.getText());
                        if (raum.length()>0) {
                            Intent intent = new Intent(Hauptmenue.this, Scannen.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("KEY_RAUM", raum);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(Hauptmenue.this, getResources().getString(R.string.hauptstring_raumleer), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.string_abbrechen), null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        btn_anzeigen.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(DateiHelper.gibRaumListe().size() > 0)
                    startActivity(new Intent(Hauptmenue.this, Anzeige.class));
                else{
                    Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.hauptstring_keineraueme), Snackbar.LENGTH_LONG).show();
                }
            }
        }) ;
    }
}
