package com.example.digitalreceipts;

// This is the receipt item class. Feel free to change all to String
// TODO: Swap all to string, consult ML team first!
// TODO: Parse everything to json format prior to database

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class ReceiptItem implements Parcelable{
    private String itemName;
    private double unitCost;
    private int quantity;
    private HashMap<String,Integer> billPeople;


    // Owner part may raise a problem. Splitting will update the database later on
    // hence owner will have its own setter
    //private String Owner;

    public ReceiptItem(String itemName, double unitCost, int quantity) {
        this.itemName = itemName;
        this.unitCost = unitCost;
        this.quantity = quantity;
        this.billPeople=new HashMap<String,Integer>();
    }

    // these get methods serve to make our life easier when creating adapters :)


    protected ReceiptItem(Parcel in) {
        itemName = in.readString();
        unitCost = in.readDouble();
        quantity = in.readInt();
        //Owner = in.readString();
    }

    public static final Creator<ReceiptItem> CREATOR = new Creator<ReceiptItem>() {
        @Override
        public ReceiptItem createFromParcel(Parcel in) {
            return new ReceiptItem(in);
        }

        @Override
        public ReceiptItem[] newArray(int size) {
            return new ReceiptItem[size];
        }
    };

    public HashMap<String, Integer> getBillPeople() {
        return billPeople;
    }

    public void setBillPeople(HashMap<String, Integer> billPeople) {
        this.billPeople = billPeople;
    }
//    public HashMap<String,Integer> addPersonRatio(String name, int ratio){
//        this.billPeople.put(name,ratio);
//        return this.billPeople;
//    }
    public void addPersonRatio(String name, int ratio){
        this.billPeople.put(name,ratio);

    }

    public String getItemName() {
        return itemName;
    }

    public double getUnitCost() {
        return unitCost;
    }

    public int getQuantity() { return quantity; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(itemName);
        dest.writeDouble(unitCost);
        dest.writeInt(quantity);
        //dest.writeString(Owner);
    }
}
