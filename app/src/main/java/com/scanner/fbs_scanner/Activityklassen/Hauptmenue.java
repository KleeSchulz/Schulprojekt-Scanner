package com.scanner.fbs_scanner.Activityklassen;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.scanner.fbs_scanner.Standardklassen.DateiHelper;
import com.scanner.fbs_scanner.R;
import com.scanner.fbs_scanner.Standardklassen.Geraet;
import com.scanner.fbs_scanner.Standardklassen.TinyDB;
import com.scanner.fbs_scanner.Standardklassen.TinyDBHelper;

import java.util.ArrayList;

public class Hauptmenue extends AppCompatActivity {

    //Todo: Logo Hauptmenü evtl. Hauptmenühintergrund weiß, AppIcon Sebastian      1
    //Todo: Alle Funktionen Testen Bugs beseitigen Sebastian            1
    //Todo: APK-Datei erzeugen! Sebastian                               1
    //Todo: ggf. Toastmessages generell durch Snackbars ersetzten (Snackbars optisch anpassen) // Auslagern Christian
    //Todo: 2 sprachige App (Englisch u. Deutsch)
    //todo Logfiles Aktionen des Useres und Fehlermeldungen (löschen, erstellen Datei, Appabstürze) Christian!
    //todo: verschiedene ActivityStates beachten (z.B. onResume, onRestart, onPause etc. ) Christian


    Button btn_raumerfassen;
    Button btn_anzeigen;
    ImageButton ib_homepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hauptmenue);

        //Setze Ausrichtung
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Zuweisungen
        btn_raumerfassen = findViewById(R.id.btn_erfassen);
        btn_anzeigen = findViewById(R.id.btn_anzeigen);
        ib_homepage = findViewById(R.id.ibtn_logo);

        final EditText taskEditText = new EditText(Hauptmenue.this);
        taskEditText.setInputType(InputType.TYPE_CLASS_TEXT );
        taskEditText.setMaxLines( 1 );

        // bei Drücken des Buttons btn_raumerfassen wird ein Eingabefeld für den Raumnamen
        // angezeigt und dessen Inhalt validiert
        btn_raumerfassen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Hauptmenue.this);
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
                        else{
                            Toast.makeText(Hauptmenue.this, getResources().getString(R.string.hauptstring_raumleer), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.string_abbrechen), null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // auch beim Drücken von Enter soll man weitergeleitet werden
        taskEditText.setOnEditorActionListener( new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE){
                    String raum = String.valueOf(taskEditText.getText());
                    if (raum.length()>0) {
                        Intent intent = new Intent(Hauptmenue.this, Scannen.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("KEY_RAUM", raum);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                    else{
                        Toast.makeText(Hauptmenue.this, getResources().getString(R.string.hauptstring_raumleer), Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        } );

        // bei Drücken des Buttons btn_anzeigen wird die Anzeigeactivity gestartet, sofern
        // bereits Räume erfasst wurden
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

        // Öffnet die FBS-Homepage im Browser
        ib_homepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.hauptstring_link_FBS)));
                startActivity(intent);
            }
        });
    }
}
