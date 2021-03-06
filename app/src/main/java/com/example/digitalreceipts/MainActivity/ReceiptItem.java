package com.example.digitalreceipts.MainActivity;

// This is the receipt item class. Feel free to change all to String
// TODO: Swap all to string, consult ML team first!
// TODO: Parse everything to json format prior to database

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

public class ReceiptItem implements Parcelable {
    private String itemName;
    private double unitCost;
    private int quantity;
    private HashMap<String, Double> ownershipTable = new HashMap<>();

    // for ownership update
    protected ReceiptItem(Parcel in) {
        itemName = in.readString();
        unitCost = in.readDouble();
        quantity = in.readInt();
        // TODO: Observe DB behaviour
        //ownershipTable = in.readHashMap(ClassLoader.getSystemClassLoader());
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

    public void setOwnershipTable(HashMap<String, Double> ownershipTable) {
        this.ownershipTable = ownershipTable;
    }

    public HashMap<String, Double> getOwnershipTable() {
        return ownershipTable;
    }

    private ReceiptItem(String itemName, double unitCost, int quantity) {
        this.itemName = itemName;
        this.unitCost = unitCost;
        this.quantity = quantity;
    }

    public static class ReceiptItemBuilder {
        private String itemName;
        private double unitCost;
        private int quantity;

        public ReceiptItemBuilder setItemName(String itemName) {
            this.itemName = itemName;
            return this;
        }

        public ReceiptItemBuilder setUnitCost(double unitCost) {
            this.unitCost = unitCost;
            return this;
        }

        public ReceiptItemBuilder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public ReceiptItem createReceiptItem() {
            return new ReceiptItem(itemName,unitCost,quantity);
        }
    }

    // these get methods serve to make our life easier when creating adapters :)


    public String getItemName() {
        return this.itemName;
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
//        dest.writeMap(ownershipTable);
    }
}
