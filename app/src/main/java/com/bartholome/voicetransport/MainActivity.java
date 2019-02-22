package com.bartholome.voicetransport;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.ActionBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext.Builder;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TransitMode;
import com.google.maps.model.TravelMode;
import com.google.maps.android.PolyUtil;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int REQUEST_CODE = 1234;

    private Button Start;
    private TextView Speech;
    private Dialog match_text_dialog;
    private ArrayList<String> matches_text;
    private String resultatVocal ="";
    private EditText startPoint, endPoint;
    private DrawerLayout mDrawerLayout;


    private DirectionsResult result;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startPoint = findViewById(R.id.start_EditText);
        endPoint = findViewById(R.id.end_EditText);
        // Drawer navigation
        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        //menuItem.setChecked(false);
                        onOptionsItemSelected(menuItem);
                        mDrawerLayout.closeDrawers();
                        return false;
                    }
                }
        );

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24px);


        Start = findViewById(R.id.start_reg);
        Speech = findViewById(R.id.speech);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Start.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isConnected()){
                        Toast.makeText(getApplicationContext(), "Veuillez donner votre station de départ ", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                        startActivityForResult(intent, REQUEST_CODE);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Veuillez vous connecter à internet pour pouvoir utiliser notre service", Toast.LENGTH_LONG).show();
                }
            }
        });
        startPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!endPoint.getText().toString().equals(""))
                {
                    try {
                        result = DirectionsApi.newRequest(getBuilder().build()).mode(TravelMode.TRANSIT)
                                .transitMode(TransitMode.TRAIN)
                                .origin(startPoint.getText().toString())
                                .destination(endPoint.getText().toString()).departureTime(Instant.now()).await();
                                addMarkersToMap(result, mMap);


                    }
                    catch(IOException  | InterruptedException | ApiException e)
                    {
                        Log.d("Erreur", "onItemClick: "+e);
                    }
                }
            }
        });
        endPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!startPoint.getText().toString().equals(""))
                {
                    try {
                        result = DirectionsApi.newRequest(getBuilder().build()).mode(TravelMode.DRIVING)
                                .origin(startPoint.getText().toString())
                                .destination(endPoint.getText().toString()).departureTime(Instant.now()).await();
                        addMarkersToMap(result, mMap);
                        addPolyline(result, mMap);

                    }
                    catch(IOException  | InterruptedException | ApiException e)
                    {
                        Log.d("Erreur", "onItemClick: "+e);
                    }
                }
            }
        });
    }
    private  boolean isConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        if (net!=null && net.isAvailable() && net.isConnected()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            match_text_dialog = new Dialog(MainActivity.this);
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle("Select Matching Text");
            ListView textlist = match_text_dialog.findViewById(R.id.list);
            matches_text = data
                    .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            ArrayAdapter<String> adapter =    new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, matches_text);
            textlist.setAdapter(adapter);
            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    Speech.setText("Vous avez dis " +matches_text.get(position));
                    resultatVocal = matches_text.get(position);
                    match_text_dialog.hide();
                    if(startPoint.getText().toString().equals(""))
                        startPoint.setText(resultatVocal);
                    else
                        endPoint.setText(resultatVocal);
                }
            });
            match_text_dialog.show();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Builder getBuilder() {
        Builder geoApiContext = new Builder();
        return geoApiContext.queryRateLimit(3).apiKey("AIzaSyBuAnhRy95K8XSSehEciHxGTbrlrAtQLj8").connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS).writeTimeout(1, TimeUnit.SECONDS);
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].startLocation.lat,results.routes[0].legs[0].startLocation.lng)).title(results.routes[0].legs[0].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].endLocation.lat,results.routes[0].legs[0].endLocation.lng)).title(results.routes[0].legs[0].startAddress).snippet(getEndLocationTitle(results)));
    }
    private String getEndLocationTitle(DirectionsResult results){ return  "Time :"+ results.routes[0].legs[0].duration.humanReadable + " Distance :" + results.routes[0].legs[0].distance.humanReadable;}
   private void addPolyline(DirectionsResult results, GoogleMap mMap) {List<LatLng> decodedPath = PolyUtil.decode(results.routes[0].overviewPolyline.getEncodedPath());
        mMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.nav_advancedResearch:
                intent = new Intent(MainActivity.this, AdvancedResearchActivity.class);
                MainActivity.this.startActivity(intent);
            case R.id.nav_details:
                // TODO: Ajouter activité détails
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        LatLng Paris = new LatLng(48.8534,  2.3488);
       // mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.zoomTo(15.0f));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Paris, 15.0f));
    }

}