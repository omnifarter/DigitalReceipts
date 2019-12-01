package com.example.digitalreceipts.Billsplit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalreceipts.R;
import com.example.digitalreceipts.MainActivity.ReceiptItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BillSplitFragment extends Fragment {
    private String title;
    private int page;
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> item_names = new ArrayList<>();
    List<ReceiptItem> receiptItems;
    String receiptNumber;
    public ItemRecycleAdapter adapter;
    HashMap<String,Integer> temp_map= new HashMap<String,Integer>();


    public static BillSplitFragment newInstance(int page, String title) {
        BillSplitFragment fragmentFirst = new BillSplitFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);

//        args.putSerializable("temp_map",getTemp_map()); //doesn't work as you can't put a null object
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bill_split_layout, container, false);
        Intent intent = getActivity().getIntent();
        names = intent.getStringArrayListExtra("NAMES");
        receiptItems = intent.getParcelableArrayListExtra("BILL_SPLIT");
        adapter = new ItemRecycleAdapter(receiptItems,names,page);
        TextView name = view.findViewById(R.id.name_display);
        RecyclerView displayitems = view.findViewById(R.id.bill_split_recycler);
        displayitems.setLayoutManager(new LinearLayoutManager(context));
        name.setText(names.get(page));
        displayitems.setAdapter(adapter);
//        container.removeAllViews();
//        container.addView(view);
        return view;
    }

    public HashMap<String, Integer> getTemp_map() {
        return adapter.getItem_quantity();
    }


}
