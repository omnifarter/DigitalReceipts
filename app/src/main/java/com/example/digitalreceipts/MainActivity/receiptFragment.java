package com.example.digitalreceipts.MainActivity;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digitalreceipts.Contacts.ContactsActivity;
import com.example.digitalreceipts.Database.ReceiptsManager;
import com.example.digitalreceipts.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class receiptFragment extends Fragment {
    private ReceiptsManager receiptsManager;
    View rootView;
    public final static String BILL_KEY = "BILL_SPLIT";
    private int STORAGE_PERMISSION_CODE = 1;
    Button split_bill;
    Button add_finance;

    public receiptFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_receipts, container, false);
        // For recycler view

        RecyclerView recyclerViewReceipts = rootView.findViewById(R.id.recycler_view_receipts);
        recyclerViewReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReceipts.setHasFixedSize(true);

        final ReceiptAdapter adapter = new ReceiptAdapter();
        recyclerViewReceipts.setAdapter(adapter);

        // create the object since receiptManager extends from viewmodel
        receiptsManager = ViewModelProviders.of(this).get(ReceiptsManager.class); //check changes
        receiptsManager.getAllReceipts().observe(this, new Observer<List<ReceiptsRoom>>() {
            @Override
            public void onChanged(List<ReceiptsRoom> receiptsRooms) {
                // for recycleview when we want to display data. currently only showing data
                if (receiptsRooms.isEmpty()) {
                    ReceiptItem burger1 = new ReceiptItem("Macdoonalds burger", 3.00, 1);
                    ReceiptItem burger2 = new ReceiptItem("Macdoonalds burger upsize", 4.50, 1);
                    ReceiptItem burger3 = new ReceiptItem("Macdoonalds burger extra large", 6.00, 1);

                    ArrayList<ReceiptItem> rubbish = new ArrayList<>();
                    rubbish.add(burger1);
                    rubbish.add(burger2);
                    rubbish.add(burger3);
                    receiptsManager.insert(new ReceiptsRoom("nonsense", " more nonsense", "ridiculous company", rubbish, 13.50, "food"));
                }
                adapter.setReceipts(receiptsRooms);
            }
        });
        // This handles click on the cards
        adapter.setOnItemClickListener(new ReceiptAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            // Hands over only the array. Nothing else
            public void onItemClick(final ReceiptsRoom receipts) {
                LinearLayout viewGroup = getActivity().findViewById(R.id.linear_layout_receipt_details);

                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_receipt_details, viewGroup);
                //populating the popupView
                TextView company_name = popupView.findViewById(R.id.company_info);
                RecyclerView list_of_items = popupView.findViewById(R.id.recycler_view_itemslist);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                TextView receipt_id = popupView.findViewById(R.id.receipt_id_number);
                list_of_items.setLayoutManager(linearLayoutManager);
                list_of_items.addItemDecoration(new DividerItemDecorator(ContextCompat.getDrawable(getContext(),R.drawable.divider)));
                list_of_items.setHasFixedSize(true);
                final ItemlistAdapter adapter = new ItemlistAdapter(receipts.get_listOfItems());
                list_of_items.setAdapter(adapter);
                company_name.setText(receipts.get_company());
                receipt_id.setText(Integer.toString(receipts.getId()));
                TextView total_cost = popupView.findViewById(R.id.total_cost);
                total_cost.setText(String.format("$%.2f",receipts.get_totalCost()));
                add_finance = popupView.findViewById(R.id.add_finance);
                split_bill = popupView.findViewById(R.id.split_bill);
                add_finance.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        // TODO modify this to update database of values.
                        receipts.set_splitStatus(true);
                        receipts.set_selfTotalCost(receipts.get_totalCost());
                        receiptsManager.update(receipts);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Added to finance!", Toast.LENGTH_SHORT).show();

                    }
                });
                split_bill.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                            System.out.println(receiptsManager.getAllReceiptsInListForm());
                            System.out.println(receiptsManager.getSoongsLazyList());

                            Toast.makeText(getContext(), "bill spitting", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getActivity(), ContactsActivity.class);
                            intent.putParcelableArrayListExtra(BILL_KEY, (ArrayList<ReceiptItem>)receipts.get_listOfItems());
                            intent.putExtra("RECEIPT_NUMBER",String.valueOf(receipts.getId()));
                            startActivity(intent);
                        } else {
                            requestStoragePermission();
                        }
                    }
                });
                int width = (int) (rootView.getMeasuredWidth() * 0.9);
                String width_value = "width: " + Integer.toString(width);
                int height = (int) (rootView.getMeasuredHeight() * 0.9);
                String height_value = "height: " + Integer.toString(height);
                PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                //define view items here
                popupWindow.setAnimationStyle(R.style.Animation);
                popupWindow.setWidth(width);
                popupWindow.setHeight(height);
                popupWindow.showAtLocation(rootView,  Gravity.CENTER, 0,-50);
                fadeBackground(popupWindow);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                // Reorder function ignored Temporarily
                return false;
            }

            // Delete function implemented

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                receiptsManager.delete(adapter.getReceiptAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerViewReceipts);
        return rootView;

    }


    private void requestStoragePermission() {
        if (shouldShowRequestPermissionRationale( Manifest.permission.READ_CONTACTS)) {
            //Toast.makeText(getActivity(), "Permission box coming out", Toast.LENGTH_SHORT).show();
            new AlertDialog.Builder(getActivity())
                    .setTitle("Permission needed")
                    .setMessage("Permission is needed to split bill amongst contacts")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions( new String[]{Manifest.permission.READ_CONTACTS}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            Toast.makeText(getActivity(),"Requesting permission",Toast.LENGTH_SHORT).show();
            requestPermissions( new String[]{Manifest.permission.READ_CONTACTS}, STORAGE_PERMISSION_CODE);
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission GRANTED", Toast.LENGTH_SHORT).show();
                split_bill.callOnClick();
            } else {
                Toast.makeText(getActivity(), "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void fadeBackground(PopupWindow pop){
        View container = (View) pop.getContentView().getParent();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);

    }

}

