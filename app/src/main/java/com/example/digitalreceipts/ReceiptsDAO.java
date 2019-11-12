package com.example.digitalreceipts;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.digitalreceipts.ReceiptsRoom;

import java.util.List;

// So DAO is in charge of managing our SQL DB in an elegant way. I think this will be good
// in the long run :)

@Dao
public interface ReceiptsDAO {
    // This implementation can pass multiple objects
    @Insert
    void insert(ReceiptsRoom receipt);

    @Update
    void update(ReceiptsRoom receipt);

    @Delete
    void delete(ReceiptsRoom receipt);

    // Query means you are specifying a certain SQL command verbatim. Its good for custom commands

    @Query("DELETE FROM receipt_table")
    void deleteAllReceipts();

    @Query("SELECT * FROM receipt_table ORDER BY _receiptNumber DESC")
    // List<ReceiptsRoom> getAllReceipts();
    // Live Data version
    LiveData<List<ReceiptsRoom>> getAllReceipts();
}
