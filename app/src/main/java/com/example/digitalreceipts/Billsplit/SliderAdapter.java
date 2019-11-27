package com.example.digitalreceipts.Billsplit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.digitalreceipts.ReceiptItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SliderAdapter extends SmartFragmentStatePagerAdapter {
    Context context;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> item_names = new ArrayList<>();
    List<ReceiptItem> receiptItems;
    String receiptNumber;
    HashMap<String,Integer> temp_map= new HashMap<String,Integer>();
    @Override
    public int getCount() {
        return this.names.size();
    }

    public SliderAdapter(FragmentManager fm, Context c){
        super(fm);
        this.context = c;
        Intent intent = ((Activity) context).getIntent();
        receiptItems = intent.getParcelableArrayListExtra("BILL_SPLIT");
        this.names = intent.getStringArrayListExtra("NAMES");
        receiptNumber = intent.getStringExtra("RECEIPT_NUMBER");
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
