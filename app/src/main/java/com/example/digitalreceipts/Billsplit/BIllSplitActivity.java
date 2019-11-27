package com.example.digitalreceipts.Billsplit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.digitalreceipts.BillSplit;
import com.example.digitalreceipts.R;
import com.example.digitalreceipts.ReceiptItem;
import com.example.digitalreceipts.ReceiptsRoom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BIllSplitActivity extends AppCompatActivity {
    private ViewPager mSlideViewPager;
    private LinearLayout mDotLayout;
    SliderAdapter sliderAdapter;
    Button button;
    private TextView[] mDots;
    private BillSplit ledger;
    String receiptNumber;
    ArrayList<String> names;
    int position_prev = 0;
    HashMap<String,HashMap<String,Integer>> final_map = new HashMap<String,HashMap<String, Integer>>();
    HashMap<String,HashMap<String,Double>> to_send = new HashMap<String,HashMap<String, Double>>();
    ArrayList<ReceiptItem> receiptItems;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ledger = new BillSplit(this);   //creates an instance of billsplitting
        setContentView(R.layout.billsplitactivity);
        mSlideViewPager = findViewById(R.id.slideViewPager);
        button = findViewById(R.id.button3);
        mDotLayout = findViewById(R.id.dotsLayout);
        sliderAdapter = new SliderAdapter(getSupportFragmentManager(),this);
        mSlideViewPager.setAdapter(sliderAdapter);
        mSlideViewPager.setOffscreenPageLimit(10);
        addDotsIndictator(0);
        Intent intent = getIntent();
        names = intent.getStringArrayListExtra("NAMES");
        receiptItems =(intent.getParcelableArrayListExtra("BILL_SPLIT"));
        receiptNumber = intent.getParcelableExtra("RECEIPT_NUMBER");

        mSlideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position_prev!=position) {
                    System.out.println("position offset: " + positionOffset);
                    System.out.println("position offset pixels: " + positionOffsetPixels);
                    BillSplitFragment billSplitFragment = (BillSplitFragment)sliderAdapter.getRegisteredFragment(position_prev);
                    HashMap<String,Integer> temp_map = billSplitFragment.getTemp_map();
                    System.out.println("this is temp map: " + temp_map.toString());
                    System.out.println("this is current position: " + Integer.toString(position));
                    final_map.put(names.get(position_prev), temp_map);
                    System.out.println(final_map.toString());
                    position_prev=position;
                }
            }

            @Override
            public void onPageSelected(int position) {
                addDotsIndictator(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_send=updateLedgerItem(final_map,receiptItems);
                Intent next = new Intent(getApplicationContext(),FinalisedBillSplitActivity.class);
                next.putExtra("FINAL_MAP",to_send);
                next.putStringArrayListExtra("NAMES",names);
                startActivity(next);

            }
        });

    }

    public void addDotsIndictator(int position) {
        mDots = new TextView[sliderAdapter.getCount()];
        mDotLayout.removeAllViews();
        for (int i = 0; i < mDots.length; i++) {
            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(getResources().getColor(R.color.colorLightGray));
            mDotLayout.addView(mDots[i]);
        }

        if (mDots.length > 0) {
            mDots[position].setTextColor(getResources().getColor(R.color.colorPrimary));
        }

    }
    public HashMap<String, HashMap<String,Double>> updateLedgerItem(HashMap<String,HashMap<String,Integer>> personLedger, List<ReceiptItem> receiptTable){
        // itemLedger is item:<name:qty>
        Map<String,Map<String,Integer>> itemLedger = new HashMap<String,Map<String,Integer>>();
        HashMap<String,HashMap<String,Double>> convertedLedger = new HashMap<String,HashMap<String,Double>>();
        for (HashMap.Entry<String,HashMap<String,Integer>> personName : personLedger.entrySet()){
            String name = personName.getKey();
            Map<String,Integer> foodEntry = personName.getValue();
            for (Map.Entry<String,Integer> food : foodEntry.entrySet()){
                if (itemLedger.containsKey(food)){
                    //item:<name:food quantity>
                    Map<String,Integer> tmpItemLedger = itemLedger.get(food);
                    tmpItemLedger.put(name,food.getValue());
                    itemLedger.put(food.getKey(),tmpItemLedger);
                }
            }
        }

        for(Map.Entry<String,Map<String,Integer>> itemName : itemLedger.entrySet()){
            for (ReceiptItem receiptItem: receiptTable){
                if (receiptItem.getItemName()==itemName.getKey()){
                    int itemTotalUnit =0;
                    double itemTotalCost=receiptItem.getUnitCost();
                    HashMap<String, Double> tmpLedger = new HashMap<String,Double>();
                    for(Map.Entry<String,Integer> entry : itemName.getValue().entrySet()){
                        Integer ratioQty = entry.getValue();
                        itemTotalUnit+=ratioQty;
                    }
                    for(Map.Entry<String,Integer> entry: itemName.getValue().entrySet()){
                        String personName = entry.getKey();
                        Integer ratioQty = entry.getValue();
                        Double itemPay =  itemTotalCost/(double)(itemTotalUnit)*(double)(ratioQty);
                        tmpLedger.put(personName,itemPay);
                    }
                    convertedLedger.put(receiptItem.getItemName(),tmpLedger);
                }
            }

        }
        return convertedLedger;


    }



}
