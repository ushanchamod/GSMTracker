package com.ousl.gsm.ui.immense;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.ousl.gsm.DatabaseHelper;
import com.ousl.gsm.R;
import com.ousl.gsm.databinding.ActivityMapsImmenseBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Timer;


public class MapsActivityImmense extends FragmentActivity implements OnMapReadyCallback {

    // Declarations
    private GoogleMap mMap;

    private static Timer timer;

    String userId, title, timeGap;
    public double latitude, longitude;

    DatabaseHelper myDb;

    LinkedList<String[]> tempData;
    Chronometer chronometer;

    TelephonyManager telephonyManager;
    SignalStrength signalStrength;
    public static final int PERMISSION_REQUEST_CODE = 101;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        com.ousl.gsm.databinding.ActivityMapsImmenseBinding binding = ActivityMapsImmenseBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // Get the shared preferences instance
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        userId = sharedPreferences.getString("user_id", "default value");
        title = sharedPreferences.getString("tracking_title", "default value");
        timeGap = sharedPreferences.getString("second_for_tracking", "default value");

        if (isPermissionGranted()) {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {
            requestLocationPermission();
        }

        // get signal strength
        telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength strength) {
                super.onSignalStrengthsChanged(strength);
                signalStrength = strength;
            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        // Start the chronometer
        chronometer = findViewById(R.id.chronometer);
        chronometer.start();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);

            getCurrentLocation();
            trigger(Integer.parseInt(timeGap));
        } else {
            requestLocationPermission();
        }


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

    // create method trigger every 5 seconds
    public void trigger(int seconds){
        timer = new Timer();
        tempData = new LinkedList<>();

        // Set the schedule function
        timer.schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                int signalStrengthValue;

                if(signalStrength != null){
                    if (signalStrength.isGsm()) {
                        signalStrengthValue = signalStrength.getGsmSignalStrength();
                    }
                    else {
                        signalStrengthValue = signalStrength.getCdmaDbm();
                    }
                }
                else {
                    signalStrengthValue = 0;
                }



                String[] temp = {userId, title, String.valueOf(latitude), String.valueOf(longitude), String.valueOf(signalStrengthValue)};
                tempData.add(temp);

                Log.i("Timer", "Latitude: " + latitude + ", Longitude: " + longitude + ", User ID: " + userId+ ", Title: " + title + ", Time Gap: " + timeGap + ", Signal Strength: " + signalStrengthValue);
            }
        }, 0, seconds* 1000L);
    }

    public void stop_tracking(View view){
        chronometer.stop();
        timer.cancel();
        showConfirmationDialog();
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmation")
                .setMessage("Do you want to save data?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    saveDataInDatabase(); // User clicked "Yes"
                })
                .setNegativeButton("No", (dialog, which) -> {
                    finish(); // User clicked "No"
                })
                .setCancelable(false) // Prevent the dialog from being dismissed by tapping outside
                .show();
    }

    public void saveDataInDatabase(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");

        Date time = new Date();
        Date date = new Date();

        String time_string = timeFormatter.format(time);
        String date_string = dateFormatter.format(date);

        // Save data in database insertDataToImmenseMain table
        myDb = new DatabaseHelper(this);

        int isInsertedToImmenseMain = myDb.insertDataToImmenseMain(userId ,title, time_string, date_string);

        if (isInsertedToImmenseMain != -1){
            saveImmenseDetails(isInsertedToImmenseMain);
        }else{
            finish();
            Toast.makeText(this, "Data saving failed", Toast.LENGTH_SHORT).show();
        }
    }

    public void saveImmenseDetails(int id){
        Log.i("ImmenseMainID", String.valueOf(id));

        myDb = new DatabaseHelper(this);

        for(String[] data: tempData){
            insertIndividualData(id, data);
        }

        finish();
        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show();
    }

    public void insertIndividualData(int id, String[] data){

        if(!data[2].equals("0.0")){
            boolean isInsertedToImmenseDetails = myDb.insertDataToImmenseDescription(String.valueOf(id), data[2], data[3], data[4]);

            if(!isInsertedToImmenseDetails){
                insertIndividualData(id, data);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.i("onStop", "onStop");

        // Stop the timer
        if(timer != null){
            timer.cancel();
        }

        // Stop the chronometer
        if(chronometer != null){
            chronometer.stop();
        }
    }

}



