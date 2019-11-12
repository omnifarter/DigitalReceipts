package com.example.digitalreceipts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;

import java.util.List;

public class ReceiptDetailsActivity extends AppCompatActivity {

    public static String currentReceipt;



    // Variables for the table_items

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_details);

        /* loads the intent upon creation. Make sure that parceable items are processed AT oncreate
         i spent so much time debugging, turns out this is the issue */
        Intent i = this.getIntent();
        ArrayList<ReceiptItem> listofitems = i.getParcelableArrayListExtra("listofitems");


        // For items recyclerview

        RecyclerView recyclerViewitemlist = findViewById(R.id.recycler_view_itemslist);
        recyclerViewitemlist.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewitemlist.setHasFixedSize(true);

        final ItemlistAdapter adapter = new ItemlistAdapter(listofitems);
        recyclerViewitemlist.setAdapter(adapter);


    }
}
