package com.example.digitalreceipts.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.WriteBatch;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class FirestoreManager {
    private static String TAG = "firestore";
    public FirebaseFirestore db;

    public FirestoreManager(){
        // Access a Cloud FirestoreManager instance from your Activity
         this.db = FirebaseFirestore.getInstance();
    }

    public CollectionReference getUserRef(String user) {
        return this.db.collection(user);
    }

    public interface OnListener {
        void onFilled(Map<String, Object> result);
        void onError(Exception taskException);
    }

    public void getAllReceipts(String user, OnListener listener) {
        this.getUserRef(user).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> data = new HashMap<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                data.put(document.getId(),document.getData());
                            }
                            listener.onFilled(data);
                        } else {
                            listener.onError(task.getException());
                        }
                    }
                });
    }
/*Sample code on how to use getAllReceipts*/
/*
    fsm.getAllReceipts(this.userRef, new FirestoreManager.OnListener() {
        @Override
        public void onFilled(Map<String, Object> result) {
            //do smth with result
            Log.i("hihi",results.toString());
        }

        @Override
        public void onError(Exception taskException) {
            Log.i("hihi",taskException);
        }
    });*/

    // needs to be used within getAllReceipts
    public Map<String, Object> getReceiptByID(String[] receiptNumbers, Map<String,Object> allReceipts) {
        Map<String,Object> results = new HashMap<>();
        for (String receiptNumber : receiptNumbers) {
            try {
                results.put(receiptNumber,allReceipts.get(receiptNumber));
            } catch (Exception e) {
                Log.i(TAG,e.getMessage());
            }
        }
        return results;
    }

    public void addReceipts(String user, Map<String, Object> receipts) {
        WriteBatch batch = this.db.batch();

        for (Map.Entry<String, Object> entry : receipts.entrySet()) {
            batch.set(this.getUserRef(user).document(entry.getKey()),entry.getValue());
        }
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i(TAG,"completed commit!!");
            }
        });
    }

    public void test() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        // Create a new user with a first and last name
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Bob");
        user.put("last", "Lovelace");
        user.put("current Time", dateFormat.format(date));

        this.db.collection("users").document("test1").set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Done!!! YAYAY!!!");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"Error setting document",e);
            }
        });
    }

}
