package com.ousl.gsm.ui.slighting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.ousl.gsm.DatabaseHelper;
import com.ousl.gsm.R;

public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    String[][] data;
    DatabaseHelper myDb;

    String locations[];

    String times[];

    String strength[];
    LayoutInflater inflater;

    public CustomBaseAdapter(Context ctx, String[][] data) {
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

        contextView = inflater.inflate(R.layout.slighting_card, null);
        TextView textViewLocation = (TextView) contextView.findViewById(R.id.slighting_card_location);
        TextView textViewTime = (TextView) contextView.findViewById(R.id.slighting_card_time);
        TextView textViewStrength = (TextView) contextView.findViewById(R.id.slighting_card_strength);
        TextView textViewDate = (TextView) contextView.findViewById(R.id.slighting_card_date);

        textViewLocation.setText(data[position][1]);
        textViewTime.setText(data[position][3]);
        textViewStrength.setText(data[position][4] + " dBm");
        textViewDate.setText(data[position][2]);

        View finalContextView = contextView;
        contextView.setOnLongClickListener(v -> {
            showConfirmationDialog(data[position][0], finalContextView);
            return true;
        });

        return contextView;
    }

    private void showConfirmationDialog(String id, View contextView){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirmation")
                .setMessage("Do you want to delete this record?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // User clicked "Yes"
                    myDb = new DatabaseHelper(context);
                    boolean result = myDb.deleteDataFromSligtingTable(Integer.parseInt(id));

                    if(result){
                        Toast.makeText(context, "Record Deleted Successfully", Toast.LENGTH_SHORT).show();
                        // Hide contextView
                        contextView.setVisibility(View.GONE);

                    }else{
                        Toast.makeText(context, "Record Deletion Failed", Toast.LENGTH_SHORT).show();
                    }

                })
                .setNegativeButton("No", (dialog, which) -> {
                    // User clicked "No"
                })
                .setCancelable(false) // Prevent the dialog from being dismissed by tapping outside
                .show();
    }
}
