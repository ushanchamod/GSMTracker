package com.ousl.gsm.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.ousl.gsm.R;
import com.ousl.gsm.databinding.FragmentHelpBinding;


public class HelpFragment extends Fragment {

    private FragmentHelpBinding binding;

    // Define btn for toggle
    private ConstraintLayout faq_1_btn, faq_2_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHelpBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        // Initialize btn hear
        faq_1_btn = getView().findViewById(R.id.FAQ_toggle_btn_1);
        faq_2_btn = getView().findViewById(R.id.FAQ_toggle_btn_2);

        // Define and initialize Discription
        TextView discription1 = getView().findViewById(R.id.FAQ_one_discription);
        TextView discription2 = getView().findViewById(R.id.FAQ_two_discription);





        // Minimize all descriptions
        discription1.setVisibility(TextView.GONE);
        discription2.setVisibility(TextView.GONE);





        // Create listners
        faq_1_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TextView discription = getView().findViewById(R.id.FAQ_one_discription);
                discription1.setVisibility(discription1.getVisibility() == TextView.GONE ? TextView.VISIBLE : TextView.GONE);
            }
        });

        faq_2_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TextView discription = getView().findViewById(R.id.FAQ_one_discription);
                discription2.setVisibility(discription2.getVisibility() == TextView.GONE ? TextView.VISIBLE : TextView.GONE);
            }
        });
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}