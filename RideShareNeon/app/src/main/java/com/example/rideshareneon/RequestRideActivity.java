package com.example.rideshareneon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class RequestRideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_ride);

        TextInputEditText etPickup = findViewById(R.id.etPickup);
        TextInputEditText etDestination = findViewById(R.id.etDestination);
        Button btnFindRides = findViewById(R.id.btnFindRides);

        btnFindRides.setOnClickListener(v -> {
            String pickup = etPickup.getText().toString().trim();
            String destination = etDestination.getText().toString().trim();

            if (pickup.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "Please enter both locations", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, MatchingActivity.class);
                intent.putExtra("pickup", pickup);
                intent.putExtra("destination", destination);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }
}