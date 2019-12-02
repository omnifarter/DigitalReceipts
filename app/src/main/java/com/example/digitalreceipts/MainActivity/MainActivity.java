package com.example.digitalreceipts.MainActivity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.digitalreceipts.CameraOCR.CameraFragment;
import com.example.digitalreceipts.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
//test push from desktop

public class MainActivity extends FragmentActivity {
    private int STORAGE_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fragment defaultFragment = new receiptFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, defaultFragment).commit();

        BottomNavigationView botnav = findViewById(R.id.bot_nav);
        botnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.receipts:
                        Fragment selectedfragment = new receiptFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedfragment).commit();
                        break;

                    case R.id.Camera:
                        cameraDialog();
                        break;

                }
                return true;
            }
        });

    }

    private void cameraDialog() {
        CameraFragment cameraFragment =CameraFragment.newInstance("Camera Fragment");
        cameraFragment.show(getSupportFragmentManager(),"Camera Fragment show");

    }

}

