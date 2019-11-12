package com.example.digitalreceipts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptHolder> {
    private List<ReceiptsRoom> receipts = new ArrayList<>();
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ReceiptHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.receipt_item, parent, false);
        return new ReceiptHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiptHolder holder, int position) {

        ReceiptsRoom currentReceipt = receipts.get(position);
        holder.textreceiptNumber.setText(currentReceipt.get_receiptNumber());
        holder.textcompany.setText(currentReceipt.get_company());
        //TODO: May want to change structure of DB to store only STRING
        holder.texttotalCost.setText(String.valueOf(currentReceipt.get_totalCost()));

        //TODO: ADD date and time to data structure. Also need to consider minimal reloading of data
        holder.textindex.setText(String.valueOf(currentReceipt.getId()));

    }

    @Override
    public int getItemCount() {
        return receipts.size();
    }

    public void setReceipts(List<ReceiptsRoom> receipts) {
        this.receipts = receipts;

        // TODO: Will be changed to a recyclable method
        notifyDataSetChanged();
    }
    // for the damn list
    public ReceiptsRoom getReceiptAt(int position)
    {
        return receipts.get(position);
    }

    class ReceiptHolder extends RecyclerView.ViewHolder {
        private TextView textreceiptNumber;
        private TextView textcompany;
        private TextView texttotalCost;
        private TextView textindex;


        public ReceiptHolder(@NonNull View itemView) {
            super(itemView);
            this.textreceiptNumber = itemView.findViewById(R.id.receipt_id);
            this.textcompany = itemView.findViewById(R.id.company_info);
            this.texttotalCost = itemView.findViewById(R.id.total_cost);
            this.textindex = itemView.findViewById(R.id.index_number);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != -1) {
                        listener.onItemClick(receipts.get(position));
                    }
                }
            });
        }


    }

    public interface OnItemClickListener {
        void onItemClick(ReceiptsRoom receipts);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

}
