package com.example.digitalreceipts.Billsplit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalreceipts.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FinalBillSplitPersonAdapter extends RecyclerView.Adapter<FinalBillSplitPersonAdapter.ItemViewHolder> {
    private ArrayList<Double> receiptCosts = new ArrayList<Double>();
    private ArrayList<String> receiptItems = new ArrayList<String>();

    @NonNull
    @Override

    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_final_bill_split_person_item, parent, false);
        Context context = parent.getContext();

        return new ItemViewHolder(itemView);

    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull FinalBillSplitPersonAdapter.ItemViewHolder holder, int position) {
        holder.itemCost.setText(String.format("$%.2f", receiptCosts.get(position)));
        holder.itemName.setText(receiptItems.get(position));
    }

    @Override
    public int getItemCount() {
        return receiptCosts.size();
    }

    public FinalBillSplitPersonAdapter(HashMap<String,HashMap<String,Double>> receiptItems,String position_name){
        HashMap<String,Double> receiptthings = receiptItems.get(position_name);
        if (receiptthings != null) {
            for (Map.Entry<String,Double> map_element:receiptthings.entrySet()) {
                this.receiptItems.add(map_element.getKey());
                this.receiptCosts.add(map_element.getValue());
            }
        }
    }
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemCost;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.final_bill_item_name);
            this.itemCost = itemView.findViewById(R.id.final_bill_item_cost);

            // No listener attached. All quiet on the Western Front

        }

    }
}
