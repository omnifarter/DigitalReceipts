package com.example.digitalreceipts.CameraOCR;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TabScannerApi {

    // My APIKey. Pls don't leak.
    @Headers("apikey: Eqkr7pNM66rBYTCQKfEiV6KiDhCNNFr1pNwBHqv3KXIqigsw6WQ1I1ejPpgnkSxo")
    @GET("result/{token}")
    Call<OCRReceipt> getResults(@Path("token") String token);


//    @Headers({"accept: application/json", "apikey: C2HVbuer2wzfCYdwuNYunWtAMyDnC2vlH9U24PtH4mwgiDNWvt0Q2HUHkeK1zIHq"})
//    @POST("2/process")
//    Call<OCRPost> createOCRPost(@Body OCRPost sheikhPost);

    @Headers({"apikey: Eqkr7pNM66rBYTCQKfEiV6KiDhCNNFr1pNwBHqv3KXIqigsw6WQ1I1ejPpgnkSxo"})
    @Multipart
    @POST("2/process")
    Call<OCRPost> createOCRPost(@Part MultipartBody.Part filePart);


}