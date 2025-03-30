package com.example.rideshareneon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.rideshareneon.models.RideOffer;
import com.example.rideshareneon.models.User;
import com.example.rideshareneon.models.RideConfirmation;

public class MatchDetailsActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_details);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView tvDriverName = findViewById(R.id.tvDriverName);
        TextView tvCarDetails = findViewById(R.id.tvCarDetails);
        TextView tvRating = findViewById(R.id.tvRating);
        TextView tvRoute = findViewById(R.id.tvRoute);
        TextView tvPickupTime = findViewById(R.id.tvPickupTime);
        TextView tvPrice = findViewById(R.id.tvPrice);
        Button btnConfirmRide = findViewById(R.id.btnConfirmRide);

        String rideId = getIntent().getStringExtra("rideId");
        String userId = getIntent().getStringExtra("userId");
        
        if (rideId == null || userId == null) {
            finish();
            return;
        }

        // Fetch ride details from Firebase
        mDatabase.child("rides").child(rideId).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    RideOffer offer = dataSnapshot.getValue(RideOffer.class);
                    if (offer != null) {
                        // Display ride details
                        tvRoute.setText(String.format("From: %s\nTo: %s", 
                            offer.getStartLocation(), offer.getEndLocation()));
                        tvPickupTime.setText("Pickup Time: " + formatTime(offer.getTimestamp()));
                        tvPrice.setText("Estimated Price: $" + calculatePrice(offer));
                        
                        // Fetch driver details
                        mDatabase.child("users").child(offer.getDriverId())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {
                                    User driver = snapshot.getValue(User.class);
                                    if (driver != null) {
                                        tvDriverName.setText("Driver: " + driver.getName());
                                        tvCarDetails.setText("Vehicle: " + (driver.getUserType().equals("driver") ? "Toyota Camry" : "Private Vehicle"));
                                        tvRating.setText("Rating: 4.8 ★"); // Placeholder
                                    }
                                }
                                
                                @Override
                                public void onCancelled(DatabaseError error) {
                                    Toast.makeText(MatchDetailsActivity.this, 
                                        "Error loading driver details", Toast.LENGTH_SHORT).show();
                                }
                            });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(MatchDetailsActivity.this, 
                        "Error loading ride details", Toast.LENGTH_SHORT).show();
                }
            });

        btnConfirmRide.setOnClickListener(v -> {
            // Create ride confirmation
            RideConfirmation confirmation = new RideConfirmation(
                userId,
                rideId,
                System.currentTimeMillis(),
                "pending"
            );
            
            String confirmationId = mDatabase.child("confirmations").push().getKey();
            mDatabase.child("confirmations").child(confirmationId).setValue(confirmation)
                .addOnSuccessListener(aVoid -> {
                    Intent intent = new Intent(MatchDetailsActivity.this, PaymentActivity.class);
                    intent.putExtra("confirmationId", confirmationId);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MatchDetailsActivity.this, 
                        "Failed to confirm ride", Toast.LENGTH_SHORT).show();
                });
        });
    }

    private String formatTime(long timestamp) {
        // TODO: Implement proper time formatting
        return "30 mins";
    }

    private String calculatePrice(RideOffer offer) {
        // TODO: Implement proper pricing calculation
        return "15.00";
    }
}