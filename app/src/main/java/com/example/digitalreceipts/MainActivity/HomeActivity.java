 package com.example.digitalreceipts.MainActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.digitalreceipts.R;

import java.util.Timer;
import java.util.TimerTask;

 public class HomeActivity extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.fadein);
        imageView = findViewById(R.id.gif_loading);
        Glide.with(HomeActivity.this).load(R.drawable.splash_screen).into(imageView);
        imageView.startAnimation(animation);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        },2000);

    }
}
