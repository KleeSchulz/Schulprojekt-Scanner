package com.scanner.fbs_scanner.Activityklassen;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.scanner.fbs_scanner.R;
import com.scanner.fbs_scanner.Standardklassen.DateiHelper;


public class Anzeige extends AppCompatActivity {

    ListView lv_raeume;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anzeige);

        // Setze Ausrichtung
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Setze Actionbar
        setTitle(getResources().getString(R.string.titelstring_Anzeige));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        // todo: Wenn Datei extern hinzugefügt dann Absturz                      1
        // todo: generell Layout und Schriftgrößen anpassen                     2

        // Zuweisungen
        lv_raeume = findViewById( R.id.lv_raeume );
        refreshLayout = findViewById( R.id.swipe_refresh_layout );

        // Aktivieren der Contextmenü-Funktion und Laden der Daten
        registerForContextMenu( lv_raeume );
        ladeRaeume();

        // bei Anklicken eines Items gelangt man in die Detailansicht
        lv_raeume.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intentDetails = new Intent(Anzeige.this,Raumdetails.class);
                intentDetails.putExtra( "KEY_SELEKTIERTER_RAUM", lv_raeume.getItemAtPosition(position).toString());
                startActivity(intentDetails);
            }
        } );

        // aktualisiere die Daten bei SwipeToRefresh
        refreshLayout.setOnRefreshListener( new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ladeRaeume();
                refreshLayout.setRefreshing( false );
            }
        } );
    }

    // zeige Menü in Actionbar an
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_anzeige_actionbar_menu, menu);
        return true;
    }


    // bei einem LongClick auf ein Listitem öffnet sich ein Kontextmenü
   @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu( menu, v, menuInfo );
        //menu.setHeaderTitle(getResources().getString(R.string.anz_aktion));
        getMenuInflater().inflate( R.menu.activity_anzeige_context_menu,menu );
    }

    // beim Auswählen von Löschen wird der jeweilige Raum im Verzeichnis gelöscht
    // anschließend werden die Daten neu geladen
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.option_loeschen:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int listPosition = info.position;
                String zuLoeschenderRaum = DateiHelper.gibRaumListe().get(listPosition);
                DateiHelper.loescheDatei( zuLoeschenderRaum, Anzeige.this );
                ladeRaeume();
                return true;
            default:
                return super.onContextItemSelected( item );
        }
    }

    // legt einen Array-Adapter an und befüllt die ListView mit einem Array, welches die bisher
    // erfassten Räume beinhaltet
    private void ladeRaeume(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_anzeige_listview_item, DateiHelper.gibRaumListe());

        if(arrayAdapter.getCount() < 1){
            startActivity(new Intent(Anzeige.this, Hauptmenue.class) );
        }
        else {
            lv_raeume.setAdapter(arrayAdapter);
        }
    }

    // auf den Zurückpfeil reagieren
    // auf Actionbarmenü reagieren
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.option_loescheAlleRaeume:
                DateiHelper.loescheAlleDateien( this );
                return true;
            default:
                return super.onOptionsItemSelected( item );
        }
    }
}
