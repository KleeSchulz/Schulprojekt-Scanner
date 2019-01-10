package com.scanner.fbs_scanner.Activityklassen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.scanner.fbs_scanner.Standardklassen.App;
import com.scanner.fbs_scanner.Standardklassen.DateiHelper;
import com.scanner.fbs_scanner.R;
import com.scanner.fbs_scanner.Standardklassen.Geraet;


public class Hauptmenue extends AppCompatActivity {

    Button btn_raumerfassen;
    Button btn_anzeigen;
    ImageButton ib_homepage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hauptmenue);

        // erstelle initial den FBS-Ordner, sofern er noch nicht existiert
        DateiHelper.activityPlaceholder = Hauptmenue.this;
        DateiHelper.fordereLeseUndSchreibPermissionAn(true);

        // Setze Ausrichtung
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // Zuweisungen
        btn_raumerfassen = findViewById(R.id.btn_erfassen);
        btn_anzeigen = findViewById(R.id.btn_anzeigen);
        ib_homepage = findViewById(R.id.ibtn_logo);

        // bei Drücken des Buttons btn_raumerfassen wird ein Eingabefeld für den Raumnamen
        // angezeigt und dessen Inhalt validiert
        btn_raumerfassen.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(Hauptmenue.this);
                // auch beim Drücken von Enter soll man weitergeleitet werden
                taskEditText.setOnEditorActionListener( new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if(actionId == EditorInfo.IME_ACTION_DONE) {
                            String raum = String.valueOf(taskEditText.getText());
                            if (raum.length()>0) {
                                Intent intent = new Intent( Hauptmenue.this, Scannen.class );
                                Bundle bundle = new Bundle();
                                bundle.putString( "KEY_RAUM", raum );
                                intent.putExtras( bundle );
                                startActivity( intent );
                            }
                            else {
                                Toast.makeText(Hauptmenue.this, getResources().getString(R.string.hauptstring_raumleer), Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                        return false;
                    }
                } );
                taskEditText.setInputType(InputType.TYPE_CLASS_TEXT );
                taskEditText.setMaxLines( 1 );

                new AlertDialog.Builder( Hauptmenue.this )
                    .setTitle(getResources().getString(R.string.hauptstring_raumerfassen))
                    .setMessage(getResources().getString(R.string.hauptstring_raumnameeingabe))
                    .setView(taskEditText)
                    .setCancelable( true )
                    .setPositiveButton(getResources().getString(R.string.hauptstring_raumscannen), new DialogInterface.OnClickListener() {
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
                                    dialog.dismiss();
                                    Toast.makeText(Hauptmenue.this, getResources().getString(R.string.hauptstring_raumleer), Toast.LENGTH_SHORT).show();
                                }
                            }
                        } )
                    .setNegativeButton( App.getContext().getResources().getString(R.string.string_abbrechen), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        } )
                    .create().show();
            }
        });

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

    // nimmt das Ergebnis einer Permissionanfrage entgegen
    // diese Methode muss in jeder Activity implementiert werden, in der die Permissions angefragt werden
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == DateiHelper.STORAGE_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                DateiHelper.erstelleVerzeichnis();
                Toast.makeText(this, getResources().getString(R.string.scan_berecher), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getResources().getString(R.string.scan_berechver), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
