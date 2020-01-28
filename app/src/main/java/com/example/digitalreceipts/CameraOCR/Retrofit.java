package com.example.digitalreceipts.CameraOCR;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Retrofit {

    // My APIKey. Pls don't leak.
    //@Headers("apikey: k8FtJLEB9adxy6nF5tCLlqh44rCEcg4iZBdlyrTTMq8IxTy1gpUwmqW55OTgEci4")
    @Headers("apikey: 8OP9sWVfDv92AKmTRnMVjpZioDwUuh7WNlD1JItEJicZCRDUyVaXOihVliQG9oVZ")
    @GET("result/{token}")
    Call<OCRReceipt> getResults(@Path("token") String token);


//    @Headers({"accept: application/json", "apikey: C2HVbuer2wzfCYdwuNYunWtAMyDnC2vlH9U24PtH4mwgiDNWvt0Q2HUHkeK1zIHq"})
//    @POST("2/process")
//    Call<OCRPost> createOCRPost(@Body OCRPost sheikhPost);

    //@Headers({"apikey: k8FtJLEB9adxy6nF5tCLlqh44rCEcg4iZBdlyrTTMq8IxTy1gpUwmqW55OTgEci4"})
    @Headers("apikey: 8OP9sWVfDv92AKmTRnMVjpZioDwUuh7WNlD1JItEJicZCRDUyVaXOihVliQG9oVZ")
    @Multipart
    @POST("2/process")
    Call<OCRPost> createOCRPost(@Part MultipartBody.Part filePart);


//    @POST("2010-04-01/Accounts/{accountsid}/Messages")
//    Call<Void> twilioCreateMessage(@Path("accountsid") String accountSid, @Body TwilioRequestBody body);

    @FormUrlEncoded
    @POST("2010-04-01/Accounts/{accountsid}/Messages")
    Call<Void> twilioCreateMessage(@Path("accountsid") String accountSid, @Field("To") String To, @Field("From") String from, @Field("Body") String body);


    @GET("companies/find")
    Call<ClearbitResults> getCompanyInfo(@Query("domain") String companyDomain);


}