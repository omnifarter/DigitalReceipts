package com.example.digitalreceipts.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.digitalreceipts.R;

public class HomeActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView = findViewById(R.id.gif_loading);
        Glide.with(HomeActivity.this).load(R.drawable.loading_screen).into(imageView);

    }
}
