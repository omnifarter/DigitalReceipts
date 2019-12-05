package com.example.digitalreceipts.CameraOCR;


import android.util.Log;

import com.example.digitalreceipts.Database.ReceiptsManager;
import com.example.digitalreceipts.MainActivity.ReceiptsRoom;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** The following segment is part of our future plans. Rest assured, it will not be used for this 1D
 *
 */

public class ClearbitApi {
    private TabScannerApi request;
    private static ClearbitApi instance;
    private ClearbitResults results;


    private ClearbitApi() {


        // Handles all forms of initialisation for RETROFIT
        Gson gson = new GsonBuilder().serializeNulls().create();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY);


        // Clearbit requires only API for Basic Auth
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new BasicAuthInterceptor("NULL", ""))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://company.clearbit.com/v2/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        request = retrofit.create(TabScannerApi.class);
    }

    //Singleton Design Pattern!
    public static synchronized ClearbitApi getInstance() {
        if (instance == null)
            instance = new ClearbitApi();

        return instance;
    }

    /**testing phase, will add argument for dynamic link
     *
     */

    // POST request
    public void getCompanyDeeds(ReceiptsManager receiptsManager, ReceiptsRoom receiptsRoom){

        Call<ClearbitResults> call = request.getCompanyInfo("kfc.com/");
        call.enqueue(new Callback<ClearbitResults>() {
            @Override
            public void onResponse(Call<ClearbitResults> call, Response<ClearbitResults> response) {
                Log.i("Clearbit API status:  ", "Successful" );
                receiptsRoom.set_expenseType(response.body().getCategory());
                receiptsManager.update(receiptsRoom);

            }

            @Override
            public void onFailure(Call<ClearbitResults> call, Throwable t) {
                Log.i("Clearbit API status: ", "Failed" );


            }
        });


    }





}


