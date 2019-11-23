package com.example.digitalreceipts.Billsplit;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalreceipts.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;

public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.ItemlistHolder> {
    Context context;
    private ArrayList<String> listofNames = new ArrayList<>();
    String name;
    //key is person name, value is a hashmap of items to number
//    HashMap<String, HashMap<String,Integer>> splitteditems = new HashMap<String,HashMap<String, Integer>>();

    @NonNull
    @Override
    public ItemlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_split_item, parent, false);
        context=parent.getContext();
        return new ItemlistHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ItemlistHolder holder, final int position) {
        holder.itemName.setText(listofNames.get(position));
        holder.itemnumber.setText(Integer.toString(0));
        holder.numberPicker.setMin(0);
        holder.numberPicker.setMax(100);
        holder.numberPicker.setValue(0);
        holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
            //TODO ADD BILL SPLIT FUNCTIONALITY HERE
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void valueChanged(int value, ActionEnum action) {
                holder.itemnumber.setText(Integer.toString(value));
            }
        });
    }

    @Override
    public int getItemCount() {return listofNames.size();}

    public ItemRecycleAdapter(ArrayList<String> items,String name)
    {
        this.name = name;
        this.listofNames = items;

    }

    public void setListOfItems(ArrayList<String> items){
        this.listofNames = items;
        notifyDataSetChanged();
    }


    class ItemlistHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private NumberPicker numberPicker;
        private TextView itemnumber;
        public ItemlistHolder(@NonNull View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.bill_split_item_name);
            this.numberPicker = itemView.findViewById(R.id.number_picker);
            this.itemnumber = itemView.findViewById(R.id.number_reflected);

            // No listener attached. All quiet on the Western Front

        }

    }

    // This is the constructor for accepting the list of objects. IDK how optimal the
    // performance for this will be for big db





}
