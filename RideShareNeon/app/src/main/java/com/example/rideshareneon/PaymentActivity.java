package com.example.rideshareneon;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PaymentActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        String confirmationId = getIntent().getStringExtra("confirmationId");

        TextView tvRidePrice = findViewById(R.id.tvRidePrice);
        Button btnConfirmPayment = findViewById(R.id.btnConfirmPayment);

        // Set default price
        tvRidePrice.setText("Total: $15.00");

        btnConfirmPayment.setOnClickListener(v -> {
            if (confirmationId != null) {
                // Update confirmation status
                mDatabase.child("confirmations").child(confirmationId).child("status").setValue("confirmed")
                    .addOnSuccessListener(aVoid -> {
                        // Update payment details
                        mDatabase.child("confirmations").child(confirmationId).child("price").setValue(15.00);
                        mDatabase.child("confirmations").child(confirmationId).child("paymentMethod").setValue("Credit Card");
                        
                        Toast.makeText(this, "Payment confirmed!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Payment failed", Toast.LENGTH_SHORT).show();
                    });
            }
        });
    }
}