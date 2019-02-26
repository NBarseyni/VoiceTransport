package com.bartholome.voicetransport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.time.Instant;

public class AdvancedResearchActivity extends AppCompatActivity {

    EditText heureDepart;
    EditText departAvancer;
    EditText arriveeAvancer;
    Button recherecheAvancer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_research);
        heureDepart = findViewById(R.id.heureAvancer);
        departAvancer = findViewById(R.id.departAvancer);
        arriveeAvancer = findViewById(R.id.arriveAvancer);
        recherecheAvancer = findViewById(R.id.searchButton);
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        recherecheAvancer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!heureDepart.getText().toString().isEmpty() && !departAvancer.getText().toString().isEmpty() &&  !arriveeAvancer.getText().toString().isEmpty()) {
                    Instant heureArrivee = Instant.parse(heureDepart.getText().toString());
                    String departResultat = departAvancer.getText().toString();
                    String arriveeResultat = arriveeAvancer.getText().toString();

                    Intent i = new Intent(AdvancedResearchActivity.this, MainActivity.class);
                    i.putExtra("heure", heureArrivee);
                    i.putExtra("depart", departResultat);
                    i.putExtra("arrivee", arriveeResultat);
                    startActivity(i);
                }
            }
        });
        getSupportActionBar().setTitle("Recherche avancée");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

}
