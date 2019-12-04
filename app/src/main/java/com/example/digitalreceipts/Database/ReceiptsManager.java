package com.example.digitalreceipts.Database;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.digitalreceipts.MainActivity.ReceiptItem;
import com.example.digitalreceipts.MainActivity.ReceiptsRoom;

import java.util.List;

// This manager is the core part of the structure, between APP and DATABASE. That way Database can
// exist without App always on

public class ReceiptsManager extends AndroidViewModel {

    private ReceiptRepository receiptRepo;
    private LiveData<List<ReceiptsRoom>> allReceipts;

    public ReceiptsManager(@NonNull Application application) {
        super(application);
        receiptRepo = new ReceiptRepository(application);
        allReceipts = receiptRepo.getAllReceipts();
    }
    public void insert(ReceiptsRoom receipt){
        receiptRepo.insert(receipt);
    }

    public void delete(ReceiptsRoom receipt){
        receiptRepo.delete(receipt);
    }


    public void updateItemList(List<ReceiptItem> listOfItems, int receiptNumber, double selfTotalCost){
        receiptRepo.updateItemList(listOfItems, receiptNumber, selfTotalCost);
    }

    public void update(ReceiptsRoom receipt){
        receiptRepo.update(receipt);
    }

    public void deleteAllReceipts(){
        receiptRepo.deleteAllReceipts();
    }

    public LiveData<List<ReceiptsRoom>> getAllReceipts()
    {
        return allReceipts;
    }

    /** SOONG , this is for you to use. The second one will return only splitted stuffs! */

    public List<ReceiptsRoom> getAllReceiptsInListForm()
    {
        return receiptRepo.getAllReceiptsInListForm();
    }

    public List<ReceiptsRoom> getSoongsLazyList()
    {
        return receiptRepo.getSoongsLazyList();
    }
}
