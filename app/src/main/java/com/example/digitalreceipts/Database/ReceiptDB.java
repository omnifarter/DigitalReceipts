package com.example.digitalreceipts.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.digitalreceipts.ReceiptsRoom;

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
//            receiptsDAO.insert(new ReceiptsRoom("XTEST4938","null"));
//            receiptsDAO.insert(new ReceiptsRoom("XTEST74938", "null"));
//            receiptsDAO.insert(new ReceiptsRoom("XTEST7494453", "null"));
            return null;
        }
    }
}
