package com.scanner.fbs_scanner;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class Hauptmenue extends AppCompatActivity {

    //Variablen setzen
    Button btn_raumerfassen;
    TinyDB tinyDB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hauptmenue);
//==================================Zuweisung=======================================================
        btn_raumerfassen = (Button)findViewById(R.id.btn_scann);
        tinyDB = new TinyDB(this);

        Button dsf = (Button)findViewById(R.id.test2);
        dsf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get the state of your external storage
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)) {
                    // if storage is mounted return true
                    Log.e("Log", "Yes, can write to external storage.");
                    Datei.erstelleVerzeichnis("Name");

                }

            }
        });


        ///================================Neuer Raum=======================================================
        btn_raumerfassen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Context c = (Context) Hauptmenue.this; //Context Angeben  -> jeweilge Activity!
                final EditText taskEditText = new EditText( c);
                AlertDialog.Builder builder = new AlertDialog.Builder(c);
                builder.setTitle("Raum erfassen");
                builder.setMessage("Bitte Raumnamen eingeben?");
                builder.setView(taskEditText);
                builder.setPositiveButton("Raum Scannen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String raum = String.valueOf(taskEditText.getText());
                        if (raum.length()>0) {
                            Intent intent = new Intent(Hauptmenue.this, Scannen.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("RAUM", raum);
                            intent.putExtras(bundle);
                            tinyDB.putString("RAUMSAFE", raum);
                            startActivity(intent);

                        }
                        else {
                            Toast.makeText(Hauptmenue.this, "Raumnummer darf nicht leer sein", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.setNegativeButton("Abbrechen", null);
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

//==============================Prüfen ob Zwischenspeicher==========================================
        if (tinyDB.getString("DATENTYP").length() > 0){
            final Context c = (Context) Hauptmenue.this; //Context Angeben  -> jeweilge Activity!
            AlertDialog.Builder builder = new AlertDialog.Builder(c);
            builder.setTitle("Gespeicherte Daten");
            builder.setMessage("Es befinden sich noch Raumdaten im Zwischenspeicher! Scanvorgang fortzseten?");
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
            dialog.show();
        }
//===Danach löschen ggf. andere klasse=======TESTEN=================================================




    }
}
