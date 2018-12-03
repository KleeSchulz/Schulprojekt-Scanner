package com.scanner.fbs_scanner.Activityklassen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.scanner.fbs_scanner.R;
import com.scanner.fbs_scanner.Standardklassen.DateiHelper;
import com.scanner.fbs_scanner.Standardklassen.Geraet;

import java.util.ArrayList;
import java.util.Comparator;

import de.codecrafters.tableview.SortableTableView;
import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.listeners.SwipeToRefreshListener;
import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnPxWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

import static com.scanner.fbs_scanner.Standardklassen.App.getContext;

public class Raumdetails extends AppCompatActivity {

    // TODO: E-Mail versenden-Funktion im Contextmenü (Anzeige) hinzufügen

    TableView<String[]> tv_raumdetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_raumdetails );
        Intent intent = getIntent();

        //Setze Ausrichtung
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Setze Actionbar
        setTitle(getResources().getString(R.string.titelstring_raumdetails) + " " + intent.getStringExtra( "KEY_SELEKTIERTER_RAUM" ));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // Zuweisungen
        tv_raumdetails = findViewById(R.id.tableView);

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
    }

    // erstellt die Tabelle im Hinblick auf Spaltenbreite, Tabellenkopfdaten und SwipeToRefresh
    // das Setzen der Farben erfolgt über die xml-Datei
    private void erstelleTabelle(){
        // Setzen der Spaltenbreite
        TableColumnPxWidthModel columnModel = new TableColumnPxWidthModel(3, 350);
        columnModel.setColumnWidth(0,300);
        columnModel.setColumnWidth(1, 450);
        columnModel.setColumnWidth(2, 800);

        tv_raumdetails.setColumnModel( columnModel );

        // Setzen des Tabellenkopfes
        final String[] spaltennamen = { getResources().getString(R.string.raumstring_typ),getResources().getString(R.string.raumstring_inventarnummer), getResources().getString(R.string.raumstring_notiz ) };
        tv_raumdetails.setHeaderAdapter(new SimpleTableHeaderAdapter(Raumdetails.this, spaltennamen));

        // Aktivieren von SwipeToRefresh-Funktion
        tv_raumdetails.setSwipeToRefreshEnabled( true );
    }

    // Laden und Anzeigen der Daten
    private void ladeDaten(String raum){
        ArrayList<Geraet> geraeteListe = DateiHelper.leseDateiAus(raum);
        String [][] tabellendaten = new String[geraeteListe.size()][4];

        for(int i = 0; i < geraeteListe.size(); i++){
            tabellendaten[i][0] = geraeteListe.get(i).getGeraeteTyp();
            tabellendaten[i][1] = geraeteListe.get(i).getInventarnummer();
            tabellendaten[i][2] = geraeteListe.get(i).getNotiz();
        }
        tv_raumdetails.setDataAdapter(new SimpleTableDataAdapter(Raumdetails.this, tabellendaten));
    }

    // auf den Zurückpfeil reagieren
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
