package com.scanner.fbs_scanner.Activityklassen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.scanner.fbs_scanner.Standardklassen.DateiHelper;
import com.scanner.fbs_scanner.Standardklassen.Geraet;
import com.scanner.fbs_scanner.Standardklassen.IntentIntegrator;
import com.scanner.fbs_scanner.Standardklassen.IntentResult;
import com.scanner.fbs_scanner.R;


public class Scannen extends AppCompatActivity {

    //todo: Spinner, Funktion zum Hinzufügen eines Typs implementieren
    EditText et_inventarnummer,et_notiz;
    Button btn_scannen,btn_erfassungsende,btn_hinzufuegen;
    Spinner spin_typen;
    TextView tv_raumname_anz, tv_geraeteCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scannen);

        Bundle b = getIntent().getExtras();

        // Setze Ausrichtung
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setze Titel
        setTitle(getResources().getString(R.string.titelstring_Scannen) + b.get("KEY_RAUM"));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.fbsklein);

        // Zuweisungen
        et_inventarnummer = findViewById(R.id.et_inventarnummer);
        tv_raumname_anz = findViewById(R.id.tv_raumname_anz);
        spin_typen = findViewById(R.id.spin_typen);
        et_notiz = findViewById(R.id.et_notiz);
        btn_scannen = findViewById(R.id.btn_scan);
        btn_hinzufuegen = findViewById(R.id.btn_hinzufuegen);
        btn_erfassungsende = findViewById(R.id.btn_erfassungsende);
        tv_geraeteCounter = findViewById(R.id.tv_geraetecounter);

        // Übergabe des Raums
        String raum = b.getString("KEY_RAUM");
        tv_raumname_anz.setText(raum);

        // hier erfolgt der Scanvorgang über den Barcodescanner
        // das Ergebnis wird in der Methode onActivityResult entgegengenommen
        btn_scannen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(Scannen.this);
                integrator.initiateScan();
            }
        });

        // Gerät wird einer Liste hinzugefügt (noch nicht persistent)
        // zuvor findet eine Validierung der Felder statt
        btn_hinzufuegen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_raumname_anz.getText().toString().length() == 0){
                    Toast.makeText(Scannen.this, getResources().getString(R.string.scan_raumleer), Toast.LENGTH_SHORT).show();
                }
                else if (et_inventarnummer.getText().toString().length() == 0){
                    Toast.makeText(Scannen.this, getResources().getString(R.string.scan_invleer), Toast.LENGTH_SHORT).show();
                }
                else {
                    Geraet geraet = new Geraet(
                            tv_raumname_anz.getText().toString(),
                            spin_typen.getSelectedItem().toString(),
                            et_inventarnummer.getText().toString(),
                            et_notiz.getText().toString().matches( "" ) ? "-" : et_notiz.getText().toString());

                    Geraet.geraeteliste.add(geraet);
                    tv_geraeteCounter.setText(getString(R.string.scan_tv_geraetecounter,String.valueOf(Geraet.geraeteliste.size())));

                    // Meldung: Gerät hinzugefügt
                    Toast.makeText(Scannen.this, getResources().getString(R.string.scan_add), Toast.LENGTH_SHORT).show();

                    //Leeren des Inventarnummer- und Notizfeldes
                    et_inventarnummer.setText("");
                    et_notiz.setText("");
                }
            }
        });

        // wurden Geräte erfasst, dann fordere Berechtigungen an und werte die Anfrage unten in der Methode
        // onRequestPermissionsResult aus, wo das Erstellen der Datei geschieht
        // wurde nichts erfasst, frage, ob die Erfassung abgebrochen werden soll
        btn_erfassungsende.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Geraet.geraeteliste.size() > 0) {
                    DateiHelper.activityPlaceholder = Scannen.this;
                    DateiHelper.fordereLeseUndSchreibPermissionAn();
                }
                else{
                    new AlertDialog.Builder( Scannen.this )
                            .setTitle(getResources().getString(R.string.scan_abbrechen) )
                            .setMessage( getResources().getString(R.string.scan_keinegeraete) )
                            .setPositiveButton( getResources().getString(R.string.string_ja), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(new Intent(Scannen.this, Hauptmenue.class));
                                }
                            } )
                            .setNegativeButton( getResources().getString(R.string.string_nein), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            } )
                            .create().show();
                }
            }
        });
    }

    // bei Betätigen der Zurücktaste kommt folgende Meldung
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder( Scannen.this )
                .setTitle( getResources().getString(R.string.scan_abbrechen) )
                .setMessage(getResources().getString(R.string.scan_datenloeschen) )
                .setPositiveButton( getResources().getString(R.string.string_ja), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Scannen.this, Hauptmenue.class));
                    }
                } )
                .setNegativeButton( getResources().getString(R.string.string_nein                ), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } )
                .create().show();
    }

    // Behandlung des Scanergebnisses - wenn erfolgreich, befülle Inventarnummerfeld
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            String barcode = scanResult.getContents();
            et_inventarnummer.setText(barcode);
        }
    }

    // nimmt das Ergebnis einer Permissionanfrage entgegen
    // diese Methode muss in jeder Activity implementiert werden, in der die Permissions angefragt werden
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults ){

        if(requestCode == DateiHelper.STORAGE_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                DateiHelper.erzeugeUndBeschreibeDatei();
                Toast.makeText(this, getResources().getString(R.string.scan_berecher) + "\n" + getResources().getString(R.string.scan_dateierstellt),Toast.LENGTH_LONG).show();
                Geraet.geraeteliste.clear();
                startActivity(new Intent(Scannen.this, Hauptmenue.class));
            }
            else {
                Toast.makeText(this, getResources().getString(R.string.scan_berechver),Toast.LENGTH_SHORT).show();
            }
        }
    }
}