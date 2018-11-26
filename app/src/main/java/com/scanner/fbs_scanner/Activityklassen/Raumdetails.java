package com.scanner.fbs_scanner.Activityklassen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.scanner.fbs_scanner.R;
import com.scanner.fbs_scanner.Standardklassen.DateiHelper;
import com.scanner.fbs_scanner.Standardklassen.Geraet;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.model.TableColumnPxWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class Raumdetails extends AppCompatActivity {

    TableView<String[]> tv_raumdetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_raumdetails );
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        tv_raumdetails = findViewById(R.id.tableView);

        Intent intent = getIntent();
        final String selektierterRaum = intent.getStringExtra( "KEY_SELEKTIERTER_RAUM" );

        erstelleTabelle();
        ladeDaten(selektierterRaum);

        // aktualisiere die Daten bei SwipeToRefresh
        tv_raumdetails.setSwipeToRefreshListener( new SwipeToRefreshListener() {
            @Override
            public void onRefresh(RefreshIndicator refreshIndicator) {
                ladeDaten( selektierterRaum );
                refreshIndicator.hide();
            }
        } );



        // TODO: Sortierung implementieren (sowohl bei Anzeige- als auch bei Raumdetails-Activity) Christian

        // TODO: E-Mail versenden-Funktion im Contextmenü (Anzeige) hinzufügen

        // TODO: Bearbeiten der Liste ermöglichen

        // TODO: Raumanzeige im Header, dafür aus der Tabelle entfernen



    }

    // erstellt die Tabelle im Hinblick auf Spaltenbreite, Tabellenkopfdaten und SwipeToRefresh
    // das Setzen der Farben erfolgt über die xml-Datei
    private void erstelleTabelle(){
        // Setzen der Spaltenbreite
        TableColumnPxWidthModel columnModel = new TableColumnPxWidthModel(4, 350);
        columnModel.setColumnWidth(0,300);
        columnModel.setColumnWidth(1, 300);
        columnModel.setColumnWidth(2, 450);
        columnModel.setColumnWidth(3, 500);
        tv_raumdetails.setColumnModel( columnModel );

        // Setzen des Tabellenkopfes
        final String[] spaltennamen = { "Raum", "Gerätetyp", "Inventarnummer", "Notiz" };
        tv_raumdetails.setHeaderAdapter(new SimpleTableHeaderAdapter(Raumdetails.this, spaltennamen));

        // Aktivieren von SwipeToRefresh-Funktion
        tv_raumdetails.setSwipeToRefreshEnabled( true );
    }

    // Laden und Anzeigen der Daten
    private void ladeDaten(String raum){

        ArrayList<Geraet> geraeteListe = DateiHelper.leseDateiAus(raum);
        String [][] tabellendaten = new String[geraeteListe.size()][4];

        for(int i = 0; i < geraeteListe.size(); i++){
            tabellendaten[i][0] = geraeteListe.get(i).getRaumName();
            tabellendaten[i][1] = geraeteListe.get(i).getGeraeteTyp();
            tabellendaten[i][2] = geraeteListe.get(i).getInventarnummer();
            tabellendaten[i][3] = geraeteListe.get(i).getNotiz();
        }
        tv_raumdetails.setDataAdapter(new SimpleTableDataAdapter(Raumdetails.this, tabellendaten));
    }
}
