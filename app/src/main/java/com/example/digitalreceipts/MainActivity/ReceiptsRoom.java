package com.example.digitalreceipts.MainActivity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.digitalreceipts.Database.ReceiptTypeConverters;

import java.util.List;

/* This data structure I feel is best for our project. It helps with lookup
speeds and i already have multithreading implemented so our app is guaranteed
to not freeze because sql operations are forced on other background threads

Items are using embedded in this case because you cant store objects in SQL Room, unless
you do an ugly method @TypeConverter. Nevertheless, ML team can change database preferences
to their needs and I can adjust accordingly :)


You can read more here:
https://developer.android.com/reference/android/arch/persistence/room/Embedded

Edit: Embedded didnt work so i swap to ugly TypeConverter method. My bad

*/


@Entity(tableName = "receipt_table")
public class ReceiptsRoom implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String _receiptNumber;

    private String _company;

    private String _receiptUri;

    private double _totalCost;

    private String _expenseType;

    private boolean _splitStatus;

    private double _selfTotalCost;


    @TypeConverters(ReceiptTypeConverters.class)
    private List<ReceiptItem> _listOfItems;

    public ReceiptsRoom(String receiptNumber, String receiptUri, String company,List<ReceiptItem> listOfItems, double totalCost, String expenseType ) {
        this._receiptNumber = receiptNumber;
        this._receiptUri = receiptUri;
        this._company = company;
        this._expenseType = expenseType;
        this._listOfItems = listOfItems;
        this._totalCost = totalCost;
        this._splitStatus = false;
        this._selfTotalCost = 0.0;
    }

    public static class ReceiptsRoomBuilder {
        private String receiptNumber;
        private String receiptUri;
        private String company;
        private List<ReceiptItem> listOfItems;
        private double totalCost;
        private String expenseType;

        public ReceiptsRoomBuilder setReceiptNumber(String receiptNumber) {
            this.receiptNumber = receiptNumber;
            return this;
        }

        public ReceiptsRoomBuilder setReceiptUri(String receiptUri) {
            this.receiptUri = receiptUri;
            return this;
        }

        public ReceiptsRoomBuilder setCompany(String company) {
            this.company = company;
            return this;
        }

        public ReceiptsRoomBuilder setListOfItems(List<ReceiptItem> listOfItems) {
            this.listOfItems = listOfItems;
            return this;
        }

        public ReceiptsRoomBuilder setTotalCost(double totalCost) {
            this.totalCost = totalCost;
            return this;
        }

        public ReceiptsRoomBuilder setExpenseType(String expenseType) {
            this.expenseType = expenseType;
            return this;
        }

        public ReceiptsRoom createReceiptsRoom() {
            return new ReceiptsRoom(receiptNumber, receiptUri, company, listOfItems, totalCost, expenseType);
        }
    }


    protected ReceiptsRoom(Parcel in) {
        id = in.readInt();
        _receiptNumber = in.readString();
        _company = in.readString();
        _receiptUri = in.readString();
        _totalCost = in.readDouble();
        _expenseType = in.readString();
        _splitStatus = in.readBoolean();
        _selfTotalCost = in.readDouble();
        // in.readParcelableList(_listOfItems, ClassLoader.getSystemClassLoader());
    }

    public static final Creator<ReceiptsRoom> CREATOR = new Creator<ReceiptsRoom>() {
        @Override
        public ReceiptsRoom createFromParcel(Parcel in) {
            return new ReceiptsRoom(in);
        }

        @Override
        public ReceiptsRoom[] newArray(int size) {
            return new ReceiptsRoom[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String get_receiptNumber() {
        return _receiptNumber;
    }
    public String get_receiptUri() {
        return _receiptUri;
    }

    public String get_company() {
        return _company;
    }


    public List<ReceiptItem> get_listOfItems()
    {
        return _listOfItems;
    }

    public double get_totalCost() {
        return _totalCost;
    }

    public double get_selfTotalCost(){return _selfTotalCost;}

    //TODO: Test out the 3 newly added methods

    public String get_expenseType(){return _expenseType;}

    public boolean is_splitStatus() {
        return _splitStatus;
    }

    // The following methods are for data visualisation and splitting only.

    public void set_splitStatus(boolean _splitStatus) {
        this._splitStatus = _splitStatus;
    }

    public void set_selfTotalCost (double selfTotalCost) {this._selfTotalCost = selfTotalCost;}

    public void set_expenseType(String _expenseType) {this._expenseType = _expenseType;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(_receiptNumber);
        dest.writeString(_expenseType);
        dest.writeString(_company);
        dest.writeString(_receiptUri);
        dest.writeDouble(_totalCost);
        dest.writeList(_listOfItems);
        dest.writeBoolean(_splitStatus);
        dest.writeDouble(_selfTotalCost);
    }
}



