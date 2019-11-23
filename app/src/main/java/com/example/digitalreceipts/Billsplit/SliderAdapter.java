package com.example.digitalreceipts.Billsplit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;

import com.example.digitalreceipts.R;
import com.example.digitalreceipts.ReceiptDetailsActivity;
import com.example.digitalreceipts.ReceiptItem;
import com.example.digitalreceipts.ReceiptsRoom;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;
    ArrayList<String> names = new ArrayList<>();
    ArrayList<String> item_names = new ArrayList<>();
    ArrayList<ReceiptItem> receiptItems;
    @Override
    public int getCount() {
        return this.names.size();
    }

    public SliderAdapter(Context context){
        this.context = context;
        Intent intent = ((Activity) context).getIntent();
        receiptItems = intent.getParcelableArrayListExtra("BILL_SPLIT");
        this.names = intent.getStringArrayListExtra("NAMES");
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==(ConstraintLayout)object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.bill_split_layout,container,false);
        TextView name = view.findViewById(R.id.name_display);
        RecyclerView displayitems = view.findViewById(R.id.bill_split_recycler);
        displayitems.setLayoutManager(new LinearLayoutManager(context));
        item_names = new ArrayList<String>();
        name.setText(names.get(position));
        for (ReceiptItem i: receiptItems) {
            item_names.add(i.getItemName());
        }
        final ItemRecycleAdapter adapter = new ItemRecycleAdapter(item_names,names.get(position));
        displayitems.setAdapter(adapter);

        container.addView(view);
        return view;
    }

    public void destroyItem(ViewGroup container,int position,Object object){
        container.removeView((ConstraintLayout)object);
    }
}
