package com.ousl.gsm.ui.immense;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ousl.gsm.DatabaseHelper;
import com.ousl.gsm.R;
import com.ousl.gsm.databinding.ActivityImmenseResultBinding;

public class ImmenseResult extends FragmentActivity implements OnMapReadyCallback {

    public static GoogleMap mMap;
    private ActivityImmenseResultBinding binding;

    String id, title;
    TextView pageTitle;
    public double latitude, longitude;

    String[][] results;
    DatabaseHelper myDb;
    public static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityImmenseResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        title = intent.getStringExtra("title");
        Log.i("idResult", String.valueOf(id));

        // Set page title
        pageTitle = findViewById(R.id.immense_result_head);
        pageTitle.setText(title);



        if (isPermissionGranted()) {
            // Obtain the SupportMapFragment and get notified when the map is ready to be used.
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.result_map);
            mapFragment.getMapAsync(this);
        } else {
            requestLocationPermission();
            finish();
            startActivity(getIntent());
        }

        // Database
        myDb = new DatabaseHelper(this);
        results = myDb.retrieveDataFromImmenseDescription(Integer.parseInt(id));

        try {
            Log.i("results123", "results: " + results.length);
        }
        catch (Exception e) {
            Log.i("results123", "results: " + e.getMessage());
        }

        ListView listView = (ListView) findViewById(R.id.immense_result_list_view);
        CustomBanseAdapterImmenseResult customBaseAdapter = new CustomBanseAdapterImmenseResult(getBaseContext(), results);
        listView.setAdapter(customBaseAdapter);


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            getCurrentLocation();
        } else {
            requestLocationPermission();

            // reload the activity
            finish();
            startActivity(getIntent());

        }

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


    }


    // Check if the permission is granted
    public boolean isPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Request for the permission
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    // Get current location every 5 seconds using event listener
    public void getCurrentLocation() {
        mMap.setOnMyLocationChangeListener(location -> {
            // Get the current location coordinates
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            // Log the location details
            Log.i("LocationChange", "Latitude: " + latitude + ", Longitude: " + longitude);
        });
    }
}