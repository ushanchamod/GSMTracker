package com.ousl.gsm.ui.immense;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ousl.gsm.R;

public class CustomBaseAdapterImmense extends BaseAdapter {

    Context context;
    String[][] data;
    LayoutInflater inflater;

    public CustomBaseAdapterImmense(Context ctx, String[][] data) {
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

    @SuppressLint({"ViewHolder", "InflateParams", "SetTextI18n"})
    @Override
    public View getView(int position, View contextView, ViewGroup parent) {
        contextView = inflater.inflate(R.layout.immense_card, null);

        // get views
        TextView title = (TextView) contextView.findViewById(R.id.immense_card_title);
        TextView date_time = (TextView) contextView.findViewById(R.id.date_time);
        Button see_more_btn = (Button) contextView.findViewById(R.id.see_more_btn);

        // set values
        title.setText(data[position][2]);
        date_time.setText(data[position][4] + " | " + data[position][3]);

        // handle button press
        see_more_btn.setOnClickListener(view -> {
            String title1 = data[position][2];
            Toast.makeText(context, title1, Toast.LENGTH_SHORT).show();
        });


        return contextView;
    }
}
