package com.example.digitalreceipts.Finance;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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

import com.example.digitalreceipts.Database.ReceiptsManager;
import com.example.digitalreceipts.MainActivity.ItemlistAdapter;
import com.example.digitalreceipts.MainActivity.MainActivity;
import com.example.digitalreceipts.MainActivity.ReceiptItem;
import com.example.digitalreceipts.MainActivity.ReceiptsRoom;
import com.example.digitalreceipts.R;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.callback.OnPieSelectListener;
import com.razerdp.widget.animatedpieview.data.IPieInfo;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinanceFragment extends Fragment implements DialogInterface.OnDismissListener{
    private ReceiptsManager receiptsManager;
    PopupWindow popupWindow;
    View rootView;
    public final static String BILL_KEY = "BILL_SPLIT";
    Button split_bill;
    Button add_finance;
    private ArrayList<ReceiptsRoom> fullreceiptsRooms = new ArrayList<>();

    public FinanceFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_finance, container, false);


        // For recycler view
        RecyclerView recyclerViewReceipts = rootView.findViewById(R.id.recycler_view_receipts);
        recyclerViewReceipts.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewReceipts.setHasFixedSize(true);

        final FinanceAdapter adapter = new FinanceAdapter();
        recyclerViewReceipts.setAdapter(adapter);

        // create the object since receiptManager extends from viewmodel
        receiptsManager = ViewModelProviders.of(this).get(ReceiptsManager.class); //check changes
        fullreceiptsRooms = (ArrayList)receiptsManager.getSoongsLazyList();
        adapter.setReceipts(fullreceiptsRooms);
        // This handles click on the cards
        adapter.setOnItemClickListener(new FinanceAdapter.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            // Hands over only the array. Nothing else
            public void onItemClick(final ReceiptsRoom receipts) {
                LinearLayout viewGroup = getActivity().findViewById(R.id.linear_layout_receipt_details);

                View popupView = LayoutInflater.from(getActivity()).inflate(R.layout.finance_receipt_details, viewGroup);

                //populating the popupView
                TextView company_name = popupView.findViewById(R.id.company_info);
                RecyclerView list_of_items = popupView.findViewById(R.id.recycler_view_itemslist);
                TextView total_cost = popupView.findViewById(R.id.total_cost);
                Button categorise = popupView.findViewById(R.id.categorise);
                categorise.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CategoriseDialog categoriseDialog = CategoriseDialog.newInstance(receipts,popupWindow);
                        categoriseDialog.show(getFragmentManager(),"dialog");

                    }
                });
                list_of_items.setLayoutManager(new LinearLayoutManager(getContext()));
                list_of_items.setHasFixedSize(true);
                final ItemlistAdapter adapter = new ItemlistAdapter(receipts.get_listOfItems());
                list_of_items.setAdapter(adapter);
                company_name.setText(receipts.get_company());
                total_cost.setText(String.format("$%.2f",receipts.get_totalCost()));
                int width = (int) (rootView.getMeasuredWidth() * 0.9);
                String width_value = "width: " + Integer.toString(width);
                int height = (int) (rootView.getMeasuredHeight() * 0.9);
                String height_value = "height: " + Integer.toString(height);
                popupWindow = new PopupWindow(popupView, 0, 0, true);
                //define view items here
                popupWindow.setAnimationStyle(R.style.Animation);
                popupWindow.setWidth(width);
                popupWindow.setHeight(height);
                popupWindow.showAtLocation(rootView,  Gravity.CENTER, 0,-50);
                fadeBackground(popupWindow);
            }
        });


        // For animatedPieView
        AnimatedPieView animatedPieView = rootView.findViewById(R.id.animatedPieView);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        List<ReceiptsRoom> pieReceiptRoomList = receiptsManager.getAllReceiptsInListForm();
        Map<String,Double> pieAllData = new HashMap<String,Double>();
        for(ReceiptsRoom pieReceiptRoom: pieReceiptRoomList){
            if (pieReceiptRoom.is_splitStatus()) {
                double receiptCost = pieReceiptRoom.get_selfTotalCost();
                String expenseType = pieReceiptRoom.get_expenseType();

                if(!pieAllData.containsKey(expenseType)){
                    pieAllData.put(expenseType,receiptCost);
                }
                else{
                    pieAllData.put(expenseType,receiptCost + pieAllData.get(expenseType));
                }
            }
        }
        ArrayList<Integer> current_colors = new ArrayList<>();
        RandomColors randomColors = new RandomColors();
        for(HashMap.Entry<String,Double> piedata: pieAllData.entrySet()){
            int tempcolor = randomColors.getColor();
            //TODO: THIS IS SUPER BAD WAY OF IMPLEMENTING RANDOM COLOURS BUT OH WELL
            while(current_colors.contains(tempcolor)){
                tempcolor = randomColors.getColor();
            }
            config.addData(new SimplePieInfo(piedata.getValue(),tempcolor,piedata.getKey()));
        }
        config.duration(1000);
        config.drawText(true);
        config.splitAngle(2);
        config.textSize(40);
        config.selectListener(new OnPieSelectListener<IPieInfo>() {
            @Override
            public void onSelectPie(@NonNull IPieInfo pieInfo, boolean isFloatUp) {
                if(isFloatUp) {
                    String expense_type = pieInfo.getDesc();
                    ArrayList<ReceiptsRoom> temp_receipts = new ArrayList<>();
                    for (ReceiptsRoom receipts : fullreceiptsRooms) {
                        if (receipts.get_expenseType().equals(expense_type)) {
                            temp_receipts.add(receipts);
                        }
                    }
                    adapter.setReceipts(temp_receipts);
                }
                else{
                    for (int i = 0; i <fullreceiptsRooms.size() ; i++) {
                        System.out.println("THIS IS IN FRR: " + fullreceiptsRooms.get(i).get_company());
                    }
                    adapter.setReceipts(fullreceiptsRooms);
                }


            }
        });

        animatedPieView.applyConfig(config);
        animatedPieView.start();


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


    public void fadeBackground(PopupWindow pop){
        View container = (View) pop.getContentView().getParent();
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams p = (WindowManager.LayoutParams) container.getLayoutParams();
        p.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        p.dimAmount = 0.3f;
        wm.updateViewLayout(container, p);

    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container,new FinanceFragment()).commit();
    }

    class RandomColors {
        private Stack<Integer> recycle, colors;

        public RandomColors() {
            colors = new Stack<>();
            recycle =new Stack<>();
            recycle.addAll(Arrays.asList(
                    0xfff44336,0xffe91e63,0xff9c27b0,0xff673ab7,
                    0xff3f51b5,0xff2196f3,0xff03a9f4,0xff00bcd4,
                    0xff009688,0xff4caf50,0xff8bc34a,0xffcddc39,
                    0xffffeb3b,0xffffc107,0xffff9800,0xffff5722,
                    0xff795548,0xff9e9e9e,0xff607d8b,0xff333333
                    )
            );
        }

        public int getColor() {
            if (colors.size()==0) {
                while(!recycle.isEmpty())
                    colors.push(recycle.pop());
                Collections.shuffle(colors);
            }
            Integer c= colors.pop();
            recycle.push(c);
            return c;
        }
    }

}
