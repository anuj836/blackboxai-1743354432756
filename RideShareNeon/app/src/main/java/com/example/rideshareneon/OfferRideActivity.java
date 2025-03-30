package com.example.rideshareneon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class OfferRideActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_ride);

        mDatabase = FirebaseDatabase.getInstance().getReference("rides");
        
        TextInputEditText etStartLocation = findViewById(R.id.etStartLocation);
        TextInputEditText etEndLocation = findViewById(R.id.etEndLocation);
        Button btnPostRide = findViewById(R.id.btnPostRide);

        btnPostRide.setOnClickListener(v -> {
            String start = etStartLocation.getText().toString().trim();
            String end = etEndLocation.getText().toString().trim();

            if (start.isEmpty() || end.isEmpty()) {
                Toast.makeText(this, "Please enter both locations", Toast.LENGTH_SHORT).show();
            } else {
                // Create ride offer object
                RideOffer offer = new RideOffer(
                    FirebaseAuth.getInstance().getCurrentUser().getUid(),
                    start,
                    end,
                    System.currentTimeMillis()
                );
                
                // Save to Firebase
                String rideId = mDatabase.push().getKey();
                mDatabase.child(rideId).setValue(offer)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Ride offer posted!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to post ride: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            }
        });
    }
}