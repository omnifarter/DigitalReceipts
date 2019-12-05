package com.example.digitalreceipts.CameraOCR;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TwilioAPI {
    private TabScannerApi request;
    private static TwilioAPI instance;


        private TwilioAPI() {


            // Handles all forms of initialisation for RETROFIT
            Gson gson = new GsonBuilder().serializeNulls().create();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

            /** Hey Shi ying, remember to update your credentials here yeah (line 49), first argument is SID, second is pass
             * Afterwards, you need to scroll down and change (line 74) to your "from" number given by Twilio
             */
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new BasicAuthInterceptor("ACab2badbbac777a4538dad09af22b663d", "8ec7662c6c034e508878368731652c3f"))
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.twilio.com/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)
                    .build();
            request = retrofit.create(TabScannerApi.class);
        }

        //Singleton Design Pattern!
        public static synchronized TwilioAPI getInstance() {
            if (instance == null)
                instance = new TwilioAPI();

            return instance;
        }

        /** Hey shi ying, just care bout this method only sendTwilioMessage*/


        // POST request
        public void sendTwilioMessage(String accountSID, String numberTo, String text) {

            Call<Void> call = request.twilioCreateMessage(accountSID,numberTo,"+12404282785", text);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    Log.i("Twilio request status: ", "Successful" );

                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.i("Twilio request status: ", "Failed" );

                }
            });
        }





    }

