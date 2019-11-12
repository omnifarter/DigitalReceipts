package com.example.digitalreceipts;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class ReceiptRepository {
    private ReceiptsDAO receiptsDAO;
    private LiveData<List<ReceiptsRoom>> allReceipts;


    // So in android, Application is a subset of context. Hence thats why ReceiptDB is handled
    // via context stuff. All this to ensure ONE instance
    public ReceiptRepository(Application application)
    {
        ReceiptDB database = ReceiptDB.getInstance(application);
        // Room auto subclasses the abstract subclass ReceiptDB. This is honestly quite dope
        receiptsDAO = database.receiptsDAO();
        allReceipts = receiptsDAO.getAllReceipts();
    }

    public void insert(ReceiptsRoom receiptsRoom)
    {
        new InsertReceiptsAsyncTask(receiptsDAO).execute(receiptsRoom);
    }
    public void update(ReceiptsRoom receiptsRoom)
    {
        new UpdateReceiptsAsyncTask(receiptsDAO).execute(receiptsRoom);
    }
    public void delete(ReceiptsRoom receiptsRoom)
    {
        new DeleteReceiptsAsyncTask(receiptsDAO).execute(receiptsRoom);
    }
    public void deleteAllReceipts()
    {
        new DelAllReceiptsAsyncTask(receiptsDAO).execute();
    }
    // this is just a getter for a private var
    public LiveData<List<ReceiptsRoom>> getAllReceipts()
    {
        return allReceipts;
    }

    // Room framework doesnt allow running DB operations on main thread. This makes the app freeze.
    // So we need create async tasks. This part honestly i dont fully understand the implementation
    // but it works so fk it

    private static class InsertReceiptsAsyncTask extends AsyncTask<ReceiptsRoom, Void, Void>
    {
        private ReceiptsDAO receiptsDAO;
        private InsertReceiptsAsyncTask(ReceiptsDAO receiptsDAO )
        {
            this.receiptsDAO = receiptsDAO;
        }
        @Override
        protected Void doInBackground(ReceiptsRoom ... receiptsRooms){
            receiptsDAO.insert(receiptsRooms[0]);
            return null;
        }
    }
    // seperate async task for update
    private static class UpdateReceiptsAsyncTask extends AsyncTask<ReceiptsRoom, Void, Void>
    {
        private ReceiptsDAO receiptsDAO;
        private UpdateReceiptsAsyncTask(ReceiptsDAO receiptsDAO )
        {
            this.receiptsDAO = receiptsDAO;
        }
        @Override
        protected Void doInBackground(ReceiptsRoom ... receiptsRooms){
            receiptsDAO.update(receiptsRooms[0]);
            return null;
        }
    }

    // seperate async task for delete
    private static class DeleteReceiptsAsyncTask extends AsyncTask<ReceiptsRoom, Void, Void>
    {
        private ReceiptsDAO receiptsDAO;
        private DeleteReceiptsAsyncTask(ReceiptsDAO receiptsDAO )
        {
            this.receiptsDAO = receiptsDAO;
        }
        @Override
        protected Void doInBackground(ReceiptsRoom ... receiptsRooms){
            receiptsDAO.delete(receiptsRooms[0]);
            return null;
        }
    }

    // seperate async task for delete
    private static class DelAllReceiptsAsyncTask extends AsyncTask<ReceiptsRoom, Void, Void>
    {
        private ReceiptsDAO receiptsDAO;
        private DelAllReceiptsAsyncTask(ReceiptsDAO receiptsDAO )
        {
            this.receiptsDAO = receiptsDAO;
        }
        @Override
        protected Void doInBackground(ReceiptsRoom ... receiptsRooms){
            receiptsDAO.deleteAllReceipts();
            return null;
        }
    }

}
