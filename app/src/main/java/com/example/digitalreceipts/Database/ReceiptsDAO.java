package com.example.digitalreceipts.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.example.digitalreceipts.MainActivity.ReceiptItem;
import com.example.digitalreceipts.MainActivity.ReceiptsRoom;

import java.util.List;

// So DAO is in charge of managing our SQL DB in an elegant way. I think this will be good
// in the long run :)

@Dao
public interface ReceiptsDAO {
    // This implementation can pass multiple objects
    @Insert
    void insert(ReceiptsRoom receipt);

    /**
     * @param receipt
     */
    @Update
    void update(ReceiptsRoom receipt);

    @Delete
    void delete(ReceiptsRoom receipt);

    // Query means you are specifying a certain SQL command verbatim. Its good for custom commands!

    @Query("SELECT * FROM receipt_table WHERE _receiptNumber LIKE :receiptNumber ")
    ReceiptsRoom searchReceiptFromNumber(String receiptNumber);

    //TODO: Test out updating of shiz. This will update based on the field 'receiptNumber'. May want
    // swap over to unique ID (autogenerated)
    @TypeConverters(ReceiptTypeConverters.class)
    @Query("UPDATE receipt_table SET _listOfItems = :listOfItems, _splitStatus = 1 WHERE id LIKE :receiptNumber")
    void updateItemList(List<ReceiptItem> listOfItems, int receiptNumber);

    @Query("DELETE FROM receipt_table")
    void deleteAllReceipts();

    // TODO: Fix such that its by uniqueID (autogenerated)
    @Query("SELECT * FROM receipt_table ORDER BY _receiptNumber DESC")
    // List<ReceiptsRoom> getAllReceipts();
    // Live Data version
    LiveData<List<ReceiptsRoom>> getAllReceipts();

    /** Added methods for Brother Soong. Does operations asynchronously*/

    @Query("SELECT * FROM receipt_table ORDER BY id DESC")
    // List<ReceiptsRoom> getAllReceipts();
    // Live Data version
    List<ReceiptsRoom> getAllReceiptsInListForm();

    @Query("SELECT * FROM receipt_table WHERE _splitStatus = 1")
    List<ReceiptsRoom> getSoongsLazyList();

}
