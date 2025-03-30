package com.example.rideshareneon;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MatchingActivity extends AppCompatActivity {
    private LottieAnimationView animationView;
    private TextView tvMatchingStatus;
    private Button btnCancelSearch;
    private DatabaseReference mDatabase;
    private Handler handler = new Handler();
    private boolean isMatching = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching);

        animationView = findViewById(R.id.animationView);
        tvMatchingStatus = findViewById(R.id.tvMatchingStatus);
        btnCancelSearch = findViewById(R.id.btnCancelSearch);
        mDatabase = FirebaseDatabase.getInstance().getReference("rides");

        // Start matching process
        startMatchingProcess();

        btnCancelSearch.setOnClickListener(v -> {
            isMatching = false;
            finish();
        });
    }

    private void startMatchingProcess() {
        // Simulate matching with delay
        handler.postDelayed(() -> {
            if (isMatching) {
                tvMatchingStatus.setText("Found potential matches!");
                animationView.setAnimation(R.raw.neon_success);
                animationView.playAnimation();
                findRealMatches();
            }
        }, 3000);
    }

    private void findRealMatches() {
        String pickup = getIntent().getStringExtra("pickup");
        String destination = getIntent().getStringExtra("destination");

        mDatabase.orderByChild("endLocation").equalTo(destination)
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && isMatching) {
                        // TODO: Show matches to user
                        tvMatchingStatus.setText("Found " + dataSnapshot.getChildrenCount() + " matches!");
                    } else if (isMatching) {
                        tvMatchingStatus.setText("No matches found");
                        animationView.setAnimation(R.raw.neon_error);
                        animationView.playAnimation();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    if (isMatching) {
                        tvMatchingStatus.setText("Error finding matches");
                        animationView.setAnimation(R.raw.neon_error);
                        animationView.playAnimation();
                    }
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isMatching = false;
        handler.removeCallbacksAndMessages(null);
    }
}