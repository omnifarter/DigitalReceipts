package com.example.digitalreceipts.Billsplit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.digitalreceipts.MainActivity.ReceiptItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SliderAdapter extends SmartFragmentStatePagerAdapter {
    private ArrayList<String> names = new ArrayList<>();
    ArrayList<String> item_names = new ArrayList<>();
    private HashMap<String,Integer> temp_map= new HashMap<String,Integer>();
    @Override
    public int getCount() {
        return this.names.size();
    }

    public SliderAdapter(FragmentManager fm, Context c){
        super(fm);
        Intent intent = ((Activity) c).getIntent();
        List<ReceiptItem> receiptItems = intent.getParcelableArrayListExtra("BILL_SPLIT");
        this.names = intent.getStringArrayListExtra("NAMES");
        String receiptNumber = intent.getStringExtra("RECEIPT_NUMBER");
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return BillSplitFragment.newInstance(position,names.get(position));

    }

    public HashMap<String, Integer> getTemp_map() {
        return temp_map;
    }
}
