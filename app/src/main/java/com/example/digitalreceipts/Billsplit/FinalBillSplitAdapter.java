package com.example.digitalreceipts.Billsplit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalreceipts.R;
import com.travijuu.numberpicker.library.NumberPicker;

import java.util.ArrayList;
import java.util.HashMap;

public class FinalBillSplitAdapter extends RecyclerView.Adapter<FinalBillSplitAdapter.ItemListHolder> {
    HashMap<String,HashMap<String,Double>> final_map;
    ArrayList<String> personNames;
    Context context;
    @NonNull
    @Override
    public ItemListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.final_bill_split_item, parent, false);
        context = parent.getContext();
        return new ItemListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FinalBillSplitAdapter.ItemListHolder holder, int position) {
        holder.personName.setText(personNames.get(position));
        FinalBillSplitPersonAdapter finalBillSplitPersonAdapter = new FinalBillSplitPersonAdapter(final_map,personNames.get(position));
        holder.recyclerView.setAdapter(finalBillSplitPersonAdapter);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public FinalBillSplitAdapter(HashMap<String,HashMap<String,Double>> final_map,ArrayList<String> personNames){
        this.final_map=final_map;
        this.personNames=personNames;
    }

    class ItemListHolder extends RecyclerView.ViewHolder {
        private TextView personName;
        private RecyclerView recyclerView;

        public ItemListHolder(@NonNull View itemView) {
            super(itemView);
            this.personName = itemView.findViewById(R.id.person_name);
            this.recyclerView = itemView.findViewById(R.id.final_bill_split_person_recycle);

            // No listener attached. All quiet on the Western Front

        }

    }

}
