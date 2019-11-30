package com.example.digitalreceipts.CameraOCR;
import com.google.gson.annotations.SerializedName;

import okhttp3.RequestBody;

/** The reason I have this class is to deal with the stupid tokenizing system that this API
 * has. If dont understand find me or see my github. I try link if got time
 */

public class OCRPost {

    private RequestBody imageFile;
    @SerializedName("token")
    private String token;

    public OCRPost(RequestBody imageURI) {
        imageFile = imageURI;

    }

    public String getToken() {
        return token;
    }
}
