package com.example.digitalreceipts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BillSplit {
    //keep a hashtable of how much each person owes and iterate through itemlist
    private HashMap<String,Double> ledger = new HashMap<String,Double>();
    Context context;
    ArrayList<String> names = new ArrayList<>();

    public BillSplit(){
        System.out.println("error! no names are selected for billsplitting");
    }
    public BillSplit(List<String> personName){
        //Step 1: initialises hashmap with 0 for starting sum owed by each person
        for (String i:personName){
            ledger.put(i,(double)(0));
        }
    }
    public BillSplit(Context context){
        //Step 1: initialises hashmap with 0 for starting sum owed by each person
        this.context = context;
        Intent intent = ((Activity) context).getIntent();
        this.names = intent.getStringArrayListExtra("NAMES");
        for (String i:this.names){
            ledger.put(i,(double)(0));
        }
    }
    //Step 2:add people to their respective item in col. billPeople
//    private ReceiptItem testri= new ReceiptItem("macdonalds burger",3,1);


    //Step 3.other iterate through each item to dynamically return the cost for each item
    public void updateLedgerItem(ReceiptItem testRI){
        double itemTotalCost =testRI.getUnitCost();
        int itemTotalUnit =0;
        for(Map.Entry<String,Integer> entry: testRI.getOwnershipTable().entrySet()){
            Integer ratioQty = entry.getValue();
            itemTotalUnit+=ratioQty;
        }
        for(Map.Entry<String,Integer> entry: testRI.getOwnershipTable().entrySet()){
            String personName = entry.getKey();
            Integer ratioQty = entry.getValue();
            Double itemPay =  itemTotalCost/(double)(itemTotalUnit)*(double)(ratioQty);
            //update itemPay for each item
            System.out.println(personName+ " now has to pay" + itemPay);
        }
    }

    //call iteration ONLY after all items in receipt have been added
    //Step 3a:iterate through receiptItem to update value owed by each person
    public void updateLedgerAll(List<ReceiptItem> testRI){
        for(ReceiptItem rc: testRI){
            double itemTotalCost =rc.getUnitCost();
            int itemTotalUnit =0;
            for(Map.Entry<String,Integer> entry: rc.getOwnershipTable().entrySet()){
                Integer ratioQty = entry.getValue();
                itemTotalUnit+=ratioQty;
            }
            for(Map.Entry<String,Integer> entry: rc.getOwnershipTable().entrySet()){
                String personName = entry.getKey();
                Integer ratioQty = entry.getValue();
                Double itemPay =  itemTotalCost/(double)(itemTotalUnit)*(double)(ratioQty);
                double personPay = ledger.get(personName)+itemPay;
                ledger.put(personName,personPay);
                //Step 3b:if receiptItem is owed by me: add entry to database
            }
        }
    }
    //Step 4: display sum owed by each person
    public void displaySumOwed(){
        for(Map.Entry<String,Double> entry: ledger.entrySet()){
            String personName = entry.getKey();
            Double personOwe = entry.getValue();
            System.out.println(personName+" owes "+Double.toString(personOwe)+".");

        }
    }
    //Step 5: remove unsplit receipt from db




}
