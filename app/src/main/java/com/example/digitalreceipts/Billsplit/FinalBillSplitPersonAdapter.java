package com.example.digitalreceipts.Billsplit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalreceipts.R;
import com.example.digitalreceipts.ReceiptItem;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FinalBillSplitPersonAdapter extends RecyclerView.Adapter<FinalBillSplitPersonAdapter.ItemViewHolder> {
    ArrayList<Double> receiptCosts;
    ArrayList<String> receiptItems;
    Context context;
    String position_name;
    @NonNull
    @Override

    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_final_bill_split_person_item, parent, false);
        context = parent.getContext();

        return new ItemViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull FinalBillSplitPersonAdapter.ItemViewHolder holder, int position) {
        holder.itemCost.setText(Double.toString(receiptCosts.get(position)));
        holder.itemName.setText(receiptItems.get(position));
    }

    @Override
    public int getItemCount() {
        return receiptCosts.size();
    }

    public FinalBillSplitPersonAdapter(HashMap<String,HashMap<String,Double>> receiptItems,String position_name){
        for (Map.Entry<String,Double> map_element:receiptItems.get(position_name).entrySet()) {
            this.receiptItems.add(map_element.getKey());
            this.receiptCosts.add(map_element.getValue());
        }
        this.position_name=position_name;
    }
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemCost;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemName = itemView.findViewById(R.id.final_bill_item_name);
            this.itemCost = itemView.findViewById(R.id.final_bill_item_cost);

            // No listener attached. All quiet on the Western Front

        }

    }
}
