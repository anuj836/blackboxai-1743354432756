package com.example.rideshareneon.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;
import com.example.rideshareneon.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Button btnRequestRide = view.findViewById(R.id.btnRequestRide);
        Button btnOfferRide = view.findViewById(R.id.btnOfferRide);

        btnRequestRide.setOnClickListener(v -> {
            // Open RequestRideActivity
            ((HomeActivity)requireActivity()).showRequestRideScreen();
        });

        btnOfferRide.setOnClickListener(v -> {
            // Open OfferRideActivity
            ((HomeActivity)requireActivity()).showOfferRideScreen();
        });

        return view;
    }
}