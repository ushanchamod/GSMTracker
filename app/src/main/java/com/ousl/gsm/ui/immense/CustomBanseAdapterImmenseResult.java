package com.ousl.gsm.ui.immense;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ousl.gsm.R;

public class CustomBanseAdapterImmenseResult extends BaseAdapter {

    Context context;
    String[][] data;
    LayoutInflater inflater;

    public CustomBanseAdapterImmenseResult(Context ctx, String[][] data) {
        this.context = ctx;
        this.data = data;
        inflater = (LayoutInflater.from(ctx));
    }

    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint({"ViewHolder", "SetTextI18n"})
    @Override
    public View getView(int position, View contextView, ViewGroup parent) {
        contextView = inflater.inflate(R.layout.activity_immense_result_card, null);

        TextView latitude = contextView.findViewById(R.id.immense_result_card_lat);
        TextView longitude = contextView.findViewById(R.id.immense_result_card_lon);
        TextView strength = contextView.findViewById(R.id.immense_result_top_signal_strength);

        double latitudeVal = Double.parseDouble((data[position][2]));
        double longitudeVal = Double.parseDouble(data[position][3]);
        double signalStrength = Double.parseDouble(data[position][4]);

        latitude.setText(String.valueOf(latitudeVal));
        longitude.setText(String.valueOf(longitudeVal));
        strength.setText(data[position][4] + " dBm");

        // set on click listener
        contextView.setOnClickListener(view -> {
            // access mMap object from ImmenseResult.java
            ImmenseResult.mMap.clear();

//            // add marker to map
//            LatLng loc = new LatLng(latitudeVal, longitudeVal);
//            ImmenseResult.mMap.addMarker(new MarkerOptions().position(loc).title(signalStrength + " dBm"));
//            ImmenseResult.mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));

            // Define the zoom level you want
            float zoomLevel = 15.0f;

            // Create a LatLng object with the desired location
            LatLng loc = new LatLng(latitudeVal, longitudeVal);

            // Create a CameraUpdate object to change the zoom level
            CameraUpdate zoomCameraUpdate = CameraUpdateFactory.newLatLngZoom(loc, zoomLevel);

            // Move the camera to the specified location with the new zoom level
            ImmenseResult.mMap.moveCamera(zoomCameraUpdate);

            // Add a marker to the map
            ImmenseResult.mMap.addMarker(new MarkerOptions().position(loc).title(signalStrength + " dBm"));

        });

        return contextView;
    }
}
