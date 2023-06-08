package com.ousl.gsm.ui.slighting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ousl.gsm.R;

public class CustomBaseAdapter extends BaseAdapter {
    Context context;
    String[][] data;

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

        return contextView;
    }
}
