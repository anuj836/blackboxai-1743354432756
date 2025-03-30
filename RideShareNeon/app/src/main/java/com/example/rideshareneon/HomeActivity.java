package com.example.rideshareneon;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fabQuickRide;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();
        bottomNavigation = findViewById(R.id.bottom_navigation);
        fabQuickRide = findViewById(R.id.fab_quick_ride);

        // Set up bottom navigation
        bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            
            switch (item.getItemId()) {
                case R.id.nav_home:
                    selectedFragment = new HomeFragment();
                    break;
                case R.id.nav_rides:
                    selectedFragment = new RidesFragment();
                    break;
                case R.id.nav_chat:
                    selectedFragment = new ChatFragment();
                    break;
                case R.id.nav_profile:
                    selectedFragment = new ProfileFragment();
                    break;
            }

            getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit();

            return true;
        });

        // Set default fragment
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.fragment_container, new HomeFragment())
            .commit();

        // Quick ride FAB click listener
        fabQuickRide.setOnClickListener(v -> {
            // Open quick ride creation dialog
            new QuickRideDialog().show(getSupportFragmentManager(), "QuickRideDialog");
        });
    }

    @Override
    public void onBackPressed() {
        // Minimize app instead of going back to login
        moveTaskToBack(true);
    }

    public void showRequestRideScreen() {
        startActivity(new Intent(this, RequestRideActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void showOfferRideScreen() {
        startActivity(new Intent(this, OfferRideActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}