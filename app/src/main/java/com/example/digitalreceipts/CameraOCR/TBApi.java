package com.example.digitalreceipts.CameraOCR;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.digitalreceipts.Database.ReceiptsManager;
import com.example.digitalreceipts.MainActivity.ReceiptItem;
import com.example.digitalreceipts.MainActivity.ReceiptsRoom;
import com.example.digitalreceipts.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.simplemented.okdelay.DelayInterceptor;
import com.simplemented.okdelay.SimpleDelayProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;


/** The following client now employs the Singleton Design Pattern. Why?
 * 1) Saves Ram
 * 2) Allows dynamic modification to UI without crashing
 */


public class TBApi {
    static Context context;
    static ImageView imageView;
    private static TBApi instance;
    private Retrofit request;
    static TextView receiptDisplay;
    static String queryToken;
    static String result;
    private ReceiptsManager receiptsManager;
    SimpleDelayProvider simpleDelayProvider = new SimpleDelayProvider(0, TimeUnit.MILLISECONDS);


    private TBApi(Context context, ImageView imageView, TextView receiptDisplay, ReceiptsManager receiptsManager) {
        this.context = context;
        this.receiptDisplay = receiptDisplay;
        this.imageView = imageView;
        // Creates new database instance
        this.receiptsManager = receiptsManager;

        // Handles all forms of initialisation for RETROFIT
        Gson gson = new GsonBuilder().serializeNulls().create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new DelayInterceptor(simpleDelayProvider))
                .addInterceptor(loggingInterceptor)
                .build();

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://api.tabscanner.com/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        request = retrofit.create(Retrofit.class);
    }

    //Singleton Design Pattern!
    public static synchronized TBApi getInstance(Context context,ImageView imageView, TextView receiptDisplay, ReceiptsManager receiptsManager) {
        if (instance == null)
            instance = new TBApi(context,imageView,receiptDisplay, receiptsManager);

        return instance;
    }



    // POST request
    public void postRequest(MultipartBody.Part filePart) {

        Call<OCRPost> call = request.createOCRPost(filePart);
        call.enqueue(new Callback<OCRPost>()
        {
            @Override
            public void onResponse (Call <OCRPost> call, Response<OCRPost> response){
            Log.i("API", "enqueuing call for POST successful");

            if (!response.isSuccessful()) {
                Log.i("API", "POST SERVER INVALIDATED, no response from POST detected");
                return;
            }

            OCRPost postResponse = response.body();
            TBApi.queryToken = postResponse.getToken();

            if (TBApi.queryToken == null) Log.i("API","queryToken is null");
            if (TBApi.queryToken != null)
            {
                getRequest();
            }
        }
            @Override
            public void onFailure (Call <OCRPost> call, Throwable t){
            Log.i("API", "POST onFailure: " + t.getMessage());
        }
        });
    }

    public void getRequest() {
        // GET request
        simpleDelayProvider.setDelay(15, TimeUnit.SECONDS);
        Call<OCRReceipt> call2 = request.getResults(TBApi.queryToken);

        call2.enqueue(new Callback<OCRReceipt>() {
            @Override
            public void onResponse(Call<OCRReceipt> call, Response<OCRReceipt> response) {
                if (!response.isSuccessful()) {
                    Log.i("hihi", "SENT BUT CANNOT RETRIEVE MAYBE SERVER DOWN IDK");
                    return;
                }
                Log.i("hihi", "onGet has started");
                OCRReceipt receiptProcessed = response.body();
                if (receiptProcessed == null) Log.i("hihi", "OCRReceipt is null");
                String company = receiptProcessed.getData().getEstablishment();
                String total_cost = receiptProcessed.getData().getTotal();
                List<OCRReceipt.Result.LineItem> itemised = receiptProcessed.getData().getLineItems();
                List<ReceiptItem> listOfReceiptItems = new ArrayList<ReceiptItem>();

                // Scans across each item in ItemList
                for (OCRReceipt.Result.LineItem items : itemised)
                {
                    // Creates new Receipt Item
                    ReceiptItem receiptItem = new ReceiptItem.ReceiptItemBuilder().setItemName(items.getDescClean()).setUnitCost(Double.valueOf(items.getLineTotal())).setQuantity(items.getQty()).createReceiptItem();
                    listOfReceiptItems.add(receiptItem);
                }
                ReceiptsRoom receiptRoom = new ReceiptsRoom.ReceiptsRoomBuilder().setReceiptNumber(receiptProcessed.getData().getDate()).setReceiptUri("NULL").setCompany(receiptProcessed.getData().getEstablishment()).setListOfItems(listOfReceiptItems).setTotalCost(Double.valueOf(receiptProcessed.getData().getTotal())).setExpenseType(receiptProcessed.getData().getExpenseType()).createReceiptsRoom();
                receiptsManager.insert(receiptRoom);
                System.out.println(receiptProcessed.getData().getExpenseType());

                String obj1_name = itemised.get(0).getDescClean();
                String obj1_price = itemised.get(0).getLineTotal();
                String obj1_qty = String.valueOf(itemised.get(0).getQty());

                TBApi.result = "company: " + company + "\n"
                        + "total cost: " + total_cost + "\n"
                        + "obj1_name: " + obj1_name + "\n"
                        + "obj1_price: " + obj1_price + "\n"
                        + "obj1_qty: " + obj1_qty;

                Log.i("hihi", "GET onResponse: " + TBApi.result);
                receiptDisplay.setText("Receipt has been uploaded!");
                imageView.setBackgroundResource(R.drawable.app_icon);
                Glide.with(context).clear(imageView);
                simpleDelayProvider.setDelay(5, TimeUnit.SECONDS);

            }

            @Override
            public void onFailure(Call<OCRReceipt> call, Throwable t) {
                Log.i("hihi", "GET onFailure: " + t.getMessage());

            }
        });

    }

    public String run(MultipartBody.Part filePart){
        //result = "so that it ain't null";
        Log.i("hihi","running postRequest");
        postRequest(filePart);
        Log.i("hihi","done with postRequest; queryToken is: " + TBApi.queryToken);


        Log.i("hihi","running getRequest; queryToken is: " + TBApi.queryToken);
        getRequest();
        Log.i("hihi","done with getRequest");
        Log.i("hihi","result: " + result);
        return TBApi.result;
    }

}
