package com.example.digitalreceipts;

import android.content.ClipData;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

public class ItemlistAdapter extends RecyclerView.Adapter<ItemlistAdapter.ItemlistHolder> {
    private List<ReceiptItem> listOfItems = new ArrayList<>();
    @NonNull
    @Override
    public ItemlistHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.table_of_items, parent, false);
        return new ItemlistHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemlistHolder holder, int position) {
        ReceiptItem currentItem = listOfItems.get(position);
        holder.itemName.setText(currentItem.getItemName());
        // TODO: Update once datatype swap approved by ML team
        holder.itemCost.setText(String.valueOf(currentItem.getUnitCost()));
        holder.itemQuantity.setText(String.valueOf(currentItem.getQuantity()));

    }

    @Override
    public int getItemCount() {return listOfItems.size();}

    public ItemlistAdapter(List<ReceiptItem> items)
    {
        this.listOfItems = items;
    }

    public void setListOfItems(List<ReceiptItem> items){
        this.listOfItems = items;
        // TODO: Will be changed to a recyclable method
        notifyDataSetChanged();
    }


    class ItemlistHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemQuantity;
        private TextView itemCost;


        public ItemlistHolder(@NonNull View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.item_name);
            this.itemQuantity = itemView.findViewById(R.id.item_quantity);
            this.itemCost = itemView.findViewById(R.id.item_cost);

            // No listener attached. All quiet on the Western Front

        }
    }

    // This is the constructor for accepting the list of objects. IDK how optimal the
    // performance for this will be for big db





}
