package com.scanner.fbs_scanner.Activityklassen;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.scanner.fbs_scanner.Standardklassen.DateiHelper;
import com.scanner.fbs_scanner.R;
import com.scanner.fbs_scanner.Standardklassen.TinyDB;

public class Hauptmenue extends AppCompatActivity {

    Button btn_raumerfassen;
    Button btn_anzeigen;

    //public static TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hauptmenue);

        // Zuweisungen
        btn_raumerfassen = findViewById(R.id.btn_erfassen);
        btn_anzeigen = findViewById(R.id.btn_anzeigen);

        //tinyDB = new TinyDB(this);

        /* bei Drücken des Buttons btn_raumerfassen wird ein Eingabefeld für den Raumnamen
           angezeigt und dessen Inhalt validiert */
        btn_raumerfassen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context c = Hauptmenue.this;
                final EditText taskEditText = new EditText(c);
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Raum erfassen");
                builder.setMessage("Bitte Raumnamen eingeben:");
                builder.setView(taskEditText);
                builder.setPositiveButton("Raum Scannen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String raum = String.valueOf(taskEditText.getText());
                        if (raum.length()>0) {
                            Intent intent = new Intent(Hauptmenue.this, Scannen.class);
                            //intent.putExtra("RAUMNAME_ALERTDIALOG",raum);

                            Bundle bundle = new Bundle();
                            bundle.putString("KEY_RAUM", raum);
                            intent.putExtras(bundle);
                            // tinyDB.putString("RAUMSAFE", raum);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(Hauptmenue.this, "Der Raumname darf nicht leer sein", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Abbrechen", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //==============================Prüfen ob Zwischenspeicher==========================================
        /*if (tinyDB.getString("DATENTYP").length() > 0){
            final Context c = (Context) Hauptmenue.this; //Context Angeben  -> jeweilge Activity!
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("Gespeicherte Daten");
            builder.setMessage("Es befinden sich noch Raumdaten im Zwischenspeicher! Scanvorgang fortsetzen?");
            builder.setPositiveButton("JA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Hauptmenue.this, Scannen.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("VOR", true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("Abbrechen", null);
            AlertDialog dialog = builder.create();
            dialog.show();*/

        }



//===Danach löschen ggf. andere klasse=======TESTEN=================================================












}
