package com.ousl.gsm.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.ousl.gsm.R;
import com.ousl.gsm.databinding.FragmentHelpBinding;


public class HelpFragment extends Fragment {

    private FragmentHelpBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHelpBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Initialize btn hear
        // Define btn for toggle
        ConstraintLayout faq_1_btn = getView().findViewById(R.id.FAQ_toggle_btn_1);
        ConstraintLayout faq_2_btn = getView().findViewById(R.id.FAQ_toggle_btn_2);
        ConstraintLayout faq_3_btn = getView().findViewById(R.id.FAQ_toggle_btn_3);

        // Define and initialize Discription

        // Discription_1
        TextView discription1 = getView().findViewById(R.id.FAQ_one_discription);
        discription1.setVisibility(TextView.GONE);

        // Discription_2
        TextView discription2_1 = getView().findViewById(R.id.FAQ_two_discription_part_one);
        discription2_1.setVisibility(TextView.GONE);
        TextView discription2_2 = getView().findViewById(R.id.FAQ_two_discription_part_two);
        discription2_2.setVisibility(TextView.GONE);
        ImageView slighting_image = getView().findViewById(R.id.FAQ_two_sligtion_mode_image);
        slighting_image.setVisibility(TextView.GONE);


        // Discription_3
        TextView discription3 = getView().findViewById(R.id.FAQ_three_discription);
        discription3.setVisibility(TextView.GONE);


        // Create listners
        faq_1_btn.setOnClickListener(view -> discription1.setVisibility(discription1.getVisibility() == TextView.GONE ? TextView.VISIBLE : TextView.GONE));

        faq_2_btn.setOnClickListener(view -> {
            discription2_1.setVisibility(discription2_1.getVisibility() == TextView.GONE ? TextView.VISIBLE : TextView.GONE);
            discription2_2.setVisibility(discription2_2.getVisibility() == TextView.GONE ? TextView.VISIBLE : TextView.GONE);
            slighting_image.setVisibility(slighting_image.getVisibility() == TextView.GONE ? TextView.VISIBLE : TextView.GONE);

        });

        faq_3_btn.setOnClickListener(view -> discription3.setVisibility(discription3.getVisibility() == TextView.GONE ? TextView.VISIBLE : TextView.GONE));
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}