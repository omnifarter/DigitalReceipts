package com.example.digitalreceipts.Billsplit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digitalreceipts.R;

import java.util.ArrayList;
import java.util.HashMap;

public class FinalisedBillSplitActivity extends AppCompatActivity {
    Intent intent;
    HashMap<String,HashMap<String,Double>> final_map;
    ArrayList<String> personNames;
    RecyclerView recyclerView;
    Button button;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalised_bill_split);
        intent = getIntent();
        personNames = intent.getStringArrayListExtra("NAMES");
        final_map = (HashMap<String,HashMap<String,Double>>)intent.getSerializableExtra("FINAL_MAP");
        System.out.println("This is final map" + final_map.toString());
        System.out.println(personNames);
        textView = findViewById(R.id.final_bill);
        button = findViewById(R.id.confirm);
        recyclerView = findViewById(R.id.final_bill_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
        });
        FinalBillSplitAdapter finalBillSplitAdapter = new FinalBillSplitAdapter(final_map,personNames);
        recyclerView.setAdapter(finalBillSplitAdapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FinalisedBillSplitActivity.this, "confirmed.", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
