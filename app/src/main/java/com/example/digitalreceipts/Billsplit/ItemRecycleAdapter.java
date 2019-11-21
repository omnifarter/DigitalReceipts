package com.example.digitalreceipts.Billsplit;

import android.content.Context;
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
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalreceipts.R;

import java.util.ArrayList;

public class ItemRecycleAdapter extends RecyclerView.Adapter<ItemRecycleAdapter.ItemlistHolder> {
    Context context;
    private ArrayList<String> listofNames = new ArrayList<>();
    @NonNull
    @Override
    public ItemlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.bill_split_item, parent, false);
        context=parent.getContext();
        return new ItemlistHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ItemlistHolder holder, int position) {
        holder.itemName.setText(listofNames.get(position));
        holder.numberPicker.setMin(0);
        holder.numberPicker.setMax(100);
        holder.numberPicker.setValueChangedListener(new ValueChangedListener() {
            @Override
            public void valueChanged(int value, ActionEnum action) {
                Toast.makeText(context,Integer.toString(value), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {return listofNames.size();}

    public ItemRecycleAdapter(ArrayList<String> items)
    {

        this.listofNames = items;

    }

    public void setListOfItems(ArrayList<String> items){
        this.listofNames = items;
        notifyDataSetChanged();
    }


    class ItemlistHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private NumberPicker numberPicker;

        public ItemlistHolder(@NonNull View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.bill_split_item_name);
            this.numberPicker = itemView.findViewById(R.id.number_picker);


            // No listener attached. All quiet on the Western Front

        }

    }

    // This is the constructor for accepting the list of objects. IDK how optimal the
    // performance for this will be for big db





}
