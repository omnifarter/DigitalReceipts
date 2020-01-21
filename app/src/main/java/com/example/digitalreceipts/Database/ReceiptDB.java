package com.example.digitalreceipts.Database;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.digitalreceipts.MainActivity.ReceiptItem;
import com.example.digitalreceipts.MainActivity.ReceiptsRoom;

import java.util.ArrayList;
import java.util.List;

// IN SQL, migration strategy has to be present. Thats why got the version thing. No need worry tho
// cos we wont be doing migrations for our project

@Database(entities = {ReceiptsRoom.class}, version = 1, exportSchema = false)
public abstract class ReceiptDB extends RoomDatabase {

    // WE are adopting a Singleton Design pattern for our ONE Database. Prof should give points for
    // this
    private static ReceiptDB instance;

    public abstract ReceiptsDAO receiptsDAO();

    // One thread at a time check, make sure no double instantiating due to seperate threads
    public static synchronized ReceiptDB getInstance(Context context)
    {
        if (instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ReceiptDB.class, "receipt_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        // if already existing instance
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void>{
        private ReceiptsDAO receiptsDAO;
        private PopulateDBAsyncTask(ReceiptDB db){
            receiptsDAO = db.receiptsDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            ReceiptItem dms = new ReceiptItem.ReceiptItemBuilder().setItemName("Pokka").setUnitCost(99.90).setQuantity(34).createReceiptItem();
            List<ReceiptItem> test = new ArrayList<ReceiptItem>();
            test.add(dms);
            Log.i("DBAsync", "Running1");
            receiptsDAO.insert(new ReceiptsRoom.ReceiptsRoomBuilder().setReceiptNumber("DEF363637").setReceiptUri("fjfi").setCompany("wfurw").setListOfItems(test).setTotalCost(55.50).setExpenseType("testing").createReceiptsRoom());
            Log.i("DBAsync", "Inserted");
//            ReceiptItem dms1 = new ReceiptItem("Walgred",12.02, 3);
//            List<ReceiptItem> test1 = new ArrayList<ReceiptItem>();
//            test1.add(dms1);
//            test1.add(dms1);
//            test1.add(dms1);
//            test1.add(dms1);
//            test1.add(dms1);
//            test1.add(dms1);
//            test1.add(dms1);
//            test1.add(dms1);
//            receiptsDAO.insert(new ReceiptsRoom("DEF363638", "fjfifo", "Sheng Siong", test1, 5.50));

            return null;
//            receiptsDAO.insert(new ReceiptsRoom("XTEST4938","null"));
//            receiptsDAO.insert(new ReceiptsRoom("XTEST74938", "null"));
//            receiptsDAO.insert(new ReceiptsRoom("XTEST7494453", "null"));

        }
    }
}
