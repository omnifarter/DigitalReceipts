package com.example.digitalreceipts.Billsplit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalreceipts.CameraOCR.TwilioAPI;
import com.example.digitalreceipts.Database.ReceiptsManager;
import com.example.digitalreceipts.R;
import com.example.digitalreceipts.MainActivity.ReceiptItem;

import java.util.ArrayList;
import java.util.HashMap;

public class FinalisedBillSplitActivity extends AppCompatActivity {
    Intent intent;
    HashMap<String,HashMap<String,Double>> final_map;
    HashMap<String,HashMap<String,Double>> final_map_items;
    HashMap<String,String> name_to_number;
    ArrayList<String> personNames;
    RecyclerView recyclerView;
    Button button;
    TextView textView;
    String receiptNumber;
    ReceiptsManager receiptsManager;
    ArrayList<ReceiptItem> receiptItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalised_bill_split);
        intent = getIntent();
        Bundle extras = intent.getExtras();
        personNames = intent.getStringArrayListExtra("NAMES");
        if (extras != null) {
            final_map_items = (HashMap<String,HashMap<String,Double>>)extras.getSerializable("FINAL_MAP_ITEMS");
            final_map = (HashMap<String,HashMap<String,Double>>)extras.getSerializable("FINAL_MAP");
            receiptNumber =extras.getString("RECEIPT_NUMBER");
            name_to_number = (HashMap<String,String>)extras.getSerializable("NAME_TO_NUMBER");
            receiptItems = extras.getParcelableArrayList("RECEIPT_ITEMS");
        }
        receiptsManager = ViewModelProviders.of(this).get(ReceiptsManager.class);
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
                    for (ReceiptItem r: receiptItems){
                        for (HashMap.Entry<String,HashMap<String,Double>> item: final_map_items.entrySet()){
                            if (item.getKey().equals(r.getItemName())){
                                r.setOwnershipTable(item.getValue());
                            }
                        }

                    }

                /** Hey gabriel, for setting the ownself amount (selfTotalCost), you can either use this method (3rd argument u pass
                 * the cost) or u can just use receiptRoomExample.set_selfTotalCost, then proceed to run receiptManager.update(receiptRoomExample)
                 * whichever convenient
                 *
                 * updateItemList(List<ReceiptItem> listOfItems, int receiptNumber, double selfTotalCost);
                 */
                receiptsManager.updateItemList(receiptItems,Integer.parseInt(receiptNumber),20.0);

                /** Hey shiying, for your side be sure to modify the TwilioAPI class before continuing. TwilioAPI class has everything
                 * you need to make things work. Debugger also attached when you run. Make sure numberTo is verified in Twilio
                 */
                TwilioAPI test = TwilioAPI.getInstance();
                test.sendTwilioMessage("ACd18def584fd1fb72bb38f443e79166a9","+6593240558", "Hi there!");



            }
        });
    }
}
