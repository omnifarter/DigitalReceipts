package com.example.digitalreceipts.Billsplit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.Person;
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
    HashMap<String,String> name_to_number = new HashMap<String, String>();
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
        receiptNumber = intent.getStringExtra("RECEIPT_NUMBER");
        name_to_number = (HashMap<String,String>) (intent.getSerializableExtra("NAME_TO_NUMBER"));
        Log.i("Finding string", "value of string (unloaded) is: " + intent.getParcelableExtra("RECEIPT_NUMBER"));
        Log.i("Finding string", "value of string (loaded) is: " + receiptNumber);




        mSlideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position_prev!=position) {
                    BillSplitFragment billSplitFragment = (BillSplitFragment)sliderAdapter.getRegisteredFragment(position_prev);
                    HashMap<String,Integer> temp_map = billSplitFragment.getTemp_map();
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
                to_send=updateLedgerPerson(updateLedgerItem(final_map,receiptItems));
                Intent next = new Intent(getApplicationContext(),FinalisedBillSplitActivity.class);
                Bundle extras = new Bundle();
                extras.putSerializable("FINAL_MAP",to_send);
                extras.putString("RECEIPT_NUMBER",receiptNumber);
                extras.putSerializable("NAME_TO_NUMBER",name_to_number);
                next.putExtras(extras);
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
        // itemLedger is name:<item:qty>
        Map<String,Map<String,Integer>> itemLedger = new HashMap<String,Map<String,Integer>>();
        HashMap<String,HashMap<String,Double>> convertedLedger = new HashMap<String,HashMap<String,Double>>();
        for (HashMap.Entry<String,HashMap<String,Integer>> personName : personLedger.entrySet()){
            String name = personName.getKey();
            Map<String,Integer> foodEntry = personName.getValue();
            for (Map.Entry<String,Integer> food : foodEntry.entrySet()){
                Map<String,Integer> tmpItemLedger = new HashMap<String,Integer>();
                if (itemLedger.containsKey(food.getKey())){
                    //item:<name:food quantity>
                    tmpItemLedger = itemLedger.get(food.getKey());
                    tmpItemLedger.put(name,food.getValue());
                    itemLedger.put(food.getKey(),tmpItemLedger);
                }else{

                    tmpItemLedger.put(name,food.getValue());
                    itemLedger.put(food.getKey(),tmpItemLedger);
                }
            }
        }

        System.out.println("THIS IS ITEM LEDGER" + itemLedger.toString());

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
                        Double itemPay =  round(itemTotalCost/(double)(itemTotalUnit)*(double)(ratioQty),2);
                        tmpLedger.put(personName,itemPay);
                    }
                    convertedLedger.put(receiptItem.getItemName(),tmpLedger);
                }
            }

        }
        return convertedLedger;


    }
    public HashMap<String, HashMap<String,Double>> updateLedgerPerson(HashMap<String,HashMap<String,Double>> convertedLedger){
        HashMap<String, HashMap<String,Double>> PersonLedger= new HashMap<String, HashMap<String,Double>>();

        for(HashMap.Entry<String,HashMap<String,Double> >item: convertedLedger.entrySet()){
            for(HashMap.Entry<String,Double> item2: item.getValue().entrySet()){
                HashMap<String,Double> tmpItemLedger = new HashMap<String, Double>();
                if (PersonLedger.containsKey(item2.getKey())){
                    //item:<name:food quantity>
                    tmpItemLedger = PersonLedger.get(item2.getKey());
                    tmpItemLedger.put(item.getKey(),item2.getValue());
                    PersonLedger.put(item2.getKey(),tmpItemLedger);
                }else{

                    tmpItemLedger.put(item.getKey(),item2.getValue());
                    PersonLedger.put(item2.getKey(),tmpItemLedger);
                }

            }
        }
        return PersonLedger;

    }
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }




}
