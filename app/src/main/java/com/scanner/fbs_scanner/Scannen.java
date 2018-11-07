package com.scanner.fbs_scanner;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class Scannen extends AppCompatActivity {

    String scanstring;
    TinyDB tinyDB;
    final static int maxzeichen = 200;
    EditText et_raum,et_lastscan,et_bemerkung;
    Button btn_scannen,btn_beenden,btn_add;
    Spinner spin_geraet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannen);
//=======================================Zuweisung==================================================
        tinyDB = new TinyDB(this);
        et_lastscan = (EditText) findViewById(R.id.et_lastScan);
        et_raum =(EditText)findViewById(R.id.et_raumname);
        spin_geraet = (Spinner)findViewById(R.id.spin_geraeet);
        et_bemerkung =(EditText)findViewById(R.id.et_bemerkung);
        btn_scannen = (Button)findViewById(R.id.btn_scan);
        btn_add = (Button)findViewById(R.id.btn_add);
        btn_beenden = (Button)findViewById(R.id.btn_end);
//=============================Uebergabe Raum=======================================================
        Bundle b = getIntent().getExtras();
        String raum = b.getString("RAUM");
        et_raum.setText(raum);
        if (b.getBoolean("VOR")){
            scanstring = tinyDB.getString("DATENTYP");
            et_raum.setText(tinyDB.getString("RAUMSAFE"));
        }
        else {
            scanstring = "";
        }
//===============================Scannen über Barcodescanner========================================
        btn_scannen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(Scannen.this);
                integrator.initiateScan();
            }
        });
//===============================Gerät hinzufügen===================================================
        //Prüfen ob Felder gefüllt (EAN und Raum und Max. Notzizlänge)
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_lastscan.getText().toString().length() == 0){
                    Toast.makeText(Scannen.this, "Barcode ist leer", Toast.LENGTH_SHORT).show();
                }
                else if (et_raum.getText().toString().length() == 0){
                    Toast.makeText(Scannen.this, "Raumnummer ist leer!", Toast.LENGTH_SHORT).show();
                }
                else if (et_bemerkung.getText().toString().length() > maxzeichen){
                    int zuviel = et_bemerkung.getText().toString().length() - maxzeichen ;
                    Toast.makeText(Scannen.this, "Das Feld hat " + zuviel +" Zeichen zu viel. Maximale Zeichenlänge:" + maxzeichen, Toast.LENGTH_SHORT).show();
                }
                else {
                    //Eintrag in die TinyDB/PrefKeys
                     scanstring = et_raum.getText().toString() + ";" + spin_geraet.toString() + ";" + et_lastscan.getText().toString() + ";" + et_bemerkung.getText().toString() + "\n" + scanstring;
                     tinyDB.putString("DATENTYP",scanstring);
                    //Leeren vom Barcode
                    et_lastscan.setText("");
                    //Meldung Gerät hinzugefügt
                    Toast.makeText(Scannen.this, "Gerät hinzugefügt", Toast.LENGTH_SHORT).show();
                }
            }
        });
//===============================Raumerfassung abschließen==========================================
        btn_beenden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Schreiben in Datei
                tinyDB.getString("DATENTYP"); //-> Der String muss eingefügt werden
                //Datei vorhanden (wenn ja überschreiben, sonst anlegen)
                //Anhängen an die Datei
                //Datenspeicher leer setzten
                tinyDB.putString("DATENTYP","");
                //Wechsel zum Hauptmenü
                startActivity(new Intent(Scannen.this, Hauptmenue.class));
            }
        });
    }
//===============================Scanvorgang abbrechen==============================================
    @Override
    public void onBackPressed() {
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Scan abbrechen und Daten löschen?");
        alertDialogBuilder.setPositiveButton("Löschen",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        tinyDB.putString("DATENTYP","");
                        startActivity(new Intent(Scannen.this,Hauptmenue.class));
                    }
                });
        alertDialogBuilder.setNegativeButton("Abbrechen",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
//========================================Scan wenn Erfolgreich=====================================
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String barcode = scanResult.getContents();
            et_lastscan.setText(barcode);
        }
    }
}