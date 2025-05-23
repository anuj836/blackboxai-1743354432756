package com.example.rideshareneon;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class ImageViewerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        ImageView imageView = findViewById(R.id.ivFullImage);
        String imageUrl = getIntent().getStringExtra("image_url");
        
        Glide.with(this)
            .load(imageUrl)
            .into(imageView);
            
        // Close on click
        imageView.setOnClickListener(v -> finish());
    }
}