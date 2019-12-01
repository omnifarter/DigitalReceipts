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
    HashMap<String,String> name_to_number;
    ArrayList<String> personNames;
    RecyclerView recyclerView;
    Button button;
    TextView textView;
    String receiptNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalised_bill_split);
        //TODO @Gabriel: This is the receiving end of the bundle... The reason need bundle is because need to transfer over the receiptID throughout as well

        intent = getIntent();
        Bundle extras = intent.getExtras();
        personNames = intent.getStringArrayListExtra("NAMES");
        final_map = (HashMap<String,HashMap<String,Double>>)extras.getSerializable("FINAL_MAP");
        receiptNumber =extras.getString("RECEIPT_NUMBER");
        name_to_number = (HashMap<String,String>)extras.getSerializable("NAME_TO_NUMBER");
        System.out.println("This is final map" + final_map.toString());
        System.out.println("Index Value is at : " + receiptNumber);
        System.out.println(personNames);
        System.out.println("This is name to number pairing" + name_to_number.toString());
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
                /** Because our DB is async, I plan to run both DB update and Payment code here. Hence, will need a
                 * ReceiptItem object ready, with all the ownershipTable info updated.
                 */

                //TODO: @ShiYing/@Gabriel get the _listofitems (List of receipt items) updated with the ownershipTable at this point so that I can do the DB command. See ReceiptItem, under setOwnershipTable for more info
                //TODO: @ShiYing/@Gabriel  prepare contact numbers of the people selected. Raised in issue #2 on github. Can close issue when completed


            }
        });
    }
}
