package com.ousl.gsm.ui.immense;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ousl.gsm.R;
import com.ousl.gsm.databinding.FragmentImmenseBinding;


public class ImmenseFragment extends Fragment {

    private FragmentImmenseBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        binding = FragmentImmenseBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Add a TextView to the fragment and set its text
//        TextView textView = root.findViewById(R.id.text_immense);
//        textView.setText("Immense Fragment");


        return root;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}