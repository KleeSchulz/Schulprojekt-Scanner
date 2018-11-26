package com.scanner.fbs_scanner.Activityklassen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.scanner.fbs_scanner.R;
import com.scanner.fbs_scanner.Standardklassen.DateiHelper;


public class Anzeige extends AppCompatActivity {

    ListView lv_raeume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anzeige);

        //Setzte Format
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //Setzte Titel
        setTitle(getResources().getString(R.string.string_Raumanzeige));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.fbsklein);
        ;
        //todo: Wenn Datei extern hinzugefügt dann Absturz                      1
        // todo: AlertDialog und EditText, bei enter Tastatur schließen         1
        // todo: generell Layout und Schriftgrößen anpassen                     2
        // Zuweisungen
        lv_raeume = findViewById( R.id.lv_raeume );

        // Aktivieren der Contextmenü-Funktion
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

        // TODO: SWIPE TO REFRESH - Funktion für ListView implementieren! Christian 1
        //Todo: Listview Oben anfagen anzeigen! Christian   3
    }



   @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu( menu, v, menuInfo );
        menu.setHeaderTitle( "Aktion wählen" );
        getMenuInflater().inflate( R.menu.activity_anzeige_context_menu,menu );

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.option_loeschen:
                AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                int listPosition = info.position;
                String zuLoeschenderRaum = DateiHelper.gibRaumListe().get(listPosition);
                DateiHelper.loescheDatei( zuLoeschenderRaum, Anzeige.this );
                ladeRaeume();
                //Toast.makeText( Anzeige.this, "Raum " + zuLoeschenderRaum + " wurde gelöscht.", Toast.LENGTH_LONG ).show();
                return true;
            default:
                return super.onContextItemSelected( item );
        }
    }

    // legt einen Array-Adapter an und befüllt die ListView mit einem Array, welches die bisher
    // erfassten Räume anzeigt
    private void ladeRaeume(){
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, R.layout.activity_anzeige_listview_item, DateiHelper.gibRaumListe());
        lv_raeume.setAdapter(arrayAdapter);

        if(arrayAdapter.getCount() < 1){
            startActivity(new Intent(Anzeige.this, Hauptmenue.class) );
        }

    }
}
