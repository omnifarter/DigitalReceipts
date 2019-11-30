package com.example.digitalreceipts;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.digitalreceipts.CameraOCR.CameraActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//test push from desktop

public class MainActivity extends FragmentActivity {
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        BottomNavigationView botnav = findViewById(R.id.bot_nav);
        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment selectedfragment = null;
                switch(menuItem.getItemId()){
                    case R.id.receipts:
                        selectedfragment = new receiptFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedfragment).commit();
                        break;


                    case R.id.Camera:
                        Intent intent = new Intent(getApplicationContext(), CameraActivity.class);
                        startActivity(intent);
                        break;

                }
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedfragment).commit();
                return true;
            }
        });

    }


}

