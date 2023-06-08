package com.ousl.gsm.ui.slighting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.ousl.gsm.DatabaseHelper;
import com.ousl.gsm.R;
import com.ousl.gsm.databinding.FragmentSlightingBinding;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SlightingFragment extends Fragment {

    private FragmentSlightingBinding binding;
    TextView signal_strength_label;
    TelephonyManager telephonyManager;
    SignalStrength signalStrength;

    DatabaseHelper myDb;

    Button saveButton;

    String location_name;

    String[][] results;

    ListView listView;

    CustomBaseAdapter customBaseAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlightingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Database
        myDb = new DatabaseHelper(getActivity());
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String userId = sharedPreferences.getString("user_id", "default value");


        results = retrieveDataFromSligtingTable(Integer.parseInt(userId));


        signal_strength_label = (TextView) root.findViewById(R.id.signal_strength_value);
        saveButton = (Button) root.findViewById(R.id.slighting_dave_button);

        telephonyManager = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new PhoneStateListener() {
            @Override
            public void onSignalStrengthsChanged(SignalStrength strength) {
                super.onSignalStrengthsChanged(strength);
                signalStrength = strength;
                updateSignalStrengthLabel();
            }
        }, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);

        listView = (ListView) root.findViewById(R.id.slighting_list_view);
        customBaseAdapter = new CustomBaseAdapter(getActivity(), results);
        listView.setAdapter(customBaseAdapter);

        handleSaveBtnPress();
        return root;
    }

    private void updateSignalStrengthLabel() {
        int signalStrengthValue;
        if (signalStrength.isGsm()) {
            signalStrengthValue = signalStrength.getGsmSignalStrength();
        } else {
            signalStrengthValue = signalStrength.getCdmaDbm();
        }

        signal_strength_label.setText(String.valueOf(signalStrengthValue));

    }

    public void handleSaveBtnPress(){
        saveButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Enter Location Name");

            // Set up the input
            final EditText input = new EditText(getActivity());
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("OK", (dialog, which) -> {
                location_name = input.getText().toString();

                // get date as string
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
                Date date = new Date();
                String date_string = dateFormatter.format(date);

                // get time as string
                @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss");
                Date time = new Date();
                String time_string = timeFormatter.format(time);

                if (location_name.isEmpty()){
                    Toast.makeText(getActivity(), "Please enter a location name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // save data to database
                myDb = new DatabaseHelper(getActivity());

                // Get the shared preferences instance
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                String userId = sharedPreferences.getString("user_id", "default value");


                boolean isInserted = myDb.insertDataToSligting(location_name, date_string, time_string, signal_strength_label.getText().toString(), userId);

                if (isInserted){
                    Toast.makeText(getActivity(), "Data saved successfully", Toast.LENGTH_SHORT).show();

                    results = retrieveDataFromSligtingTable(Integer.parseInt(userId));
                    customBaseAdapter = new CustomBaseAdapter(getActivity(), results);
                    listView.setAdapter(customBaseAdapter);
                }else{
                    Toast.makeText(getActivity(), "Data saving failed", Toast.LENGTH_SHORT).show();
                }
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

            builder.show();
        });
    }


    public String[][] retrieveDataFromSligtingTable(int use_id){
        // initialize data array
        return myDb.retrieveDataFromSligtingTable(use_id);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}