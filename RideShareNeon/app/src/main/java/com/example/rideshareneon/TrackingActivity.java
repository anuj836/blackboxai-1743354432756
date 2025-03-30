package com.example.rideshareneon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TrackingActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private DatabaseReference mDatabase;
    private String confirmationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        confirmationId = getIntent().getStringExtra("confirmationId");

        // Obtain the SupportMapFragment and get notified when the map is ready
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Button btnContactDriver = findViewById(R.id.btnContactDriver);
        btnContactDriver.setOnClickListener(v -> {
            Intent chatIntent = new Intent(this, ChatActivity.class);
            chatIntent.putExtra("confirmationId", confirmationId);
            startActivity(chatIntent);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add pickup and destination markers (sample coordinates)
        LatLng pickup = new LatLng(37.7749, -122.4194);
        LatLng destination = new LatLng(37.3352, -122.0322);
        
        mMap.addMarker(new MarkerOptions()
                .position(pickup)
                .title("Pickup Location"));
                
        mMap.addMarker(new MarkerOptions()
                .position(destination)
                .title("Destination"));

        // Draw route between points
        mMap.addPolyline(new PolylineOptions()
                .add(pickup, destination)
                .width(5)
                .color(getResources().getColor(R.color.neon_blue)));

        // Move camera to show both points
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pickup, 12));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Update ride status when activity is destroyed
        if (confirmationId != null) {
            mDatabase.child("confirmations").child(confirmationId)
                .child("status").setValue("completed");
        }
    }
}