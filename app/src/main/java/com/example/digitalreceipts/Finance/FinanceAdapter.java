package com.example.digitalreceipts.Finance;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitalreceipts.MainActivity.ReceiptsRoom;
import com.example.digitalreceipts.R;

import java.util.ArrayList;
import java.util.List;

public class FinanceAdapter extends RecyclerView.Adapter<FinanceAdapter.ReceiptHolder> {
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
        if(currentReceipt.is_splitStatus()) {
            holder.textreceiptNumber.setText(currentReceipt.get_expenseType());
            holder.textcompany.setText(currentReceipt.get_company());
            holder.texttotalCost.setText(String.format("$%.2f", currentReceipt.get_selfTotalCost()));
        }
        else{
            holder.rootView.setLayoutParams(holder.params);
        }
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
        public CardView.LayoutParams params;
        public CardView rootView;

        public ReceiptHolder(@NonNull View itemView) {
            super(itemView);
            params = new CardView.LayoutParams(0,0);
            rootView = itemView.findViewById(R.id.cardview_gone);
            this.textreceiptNumber = itemView.findViewById(R.id.receipt_id);
            this.textcompany = itemView.findViewById(R.id.company_info);
            this.texttotalCost = itemView.findViewById(R.id.total_cost);

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
