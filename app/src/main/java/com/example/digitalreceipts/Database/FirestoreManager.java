package com.example.digitalreceipts.Database;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.digitalreceipts.data.PasswordUtils;
import com.example.digitalreceipts.data.model.LoggedInUser;
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
import java.util.List;
import java.util.Map;


public class FirestoreManager {
    private static String TAG = "firestore";
    public FirebaseFirestore db;

    public FirestoreManager(){
        // Access a Cloud FirestoreManager instance from your Activity
         this.db = FirebaseFirestore.getInstance();
    }

    public interface OnListener {
        void onFilled(Object result);
        void onError(Exception taskException);
    }

    /*
    * User Info methods
    */
    public CollectionReference getUserRef(String user) {
        return this.db.collection(user);
    }

    public void getUserInfo(String user, OnListener listener) {
        this.getUserRef(user).document("userInfo").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> data = task.getResult().getData();
                            if(!(data instanceof Map)) data = new HashMap<String, Object>();
                            Log.i("loginF","FirestoreManager data: " + data.toString());
                            listener.onFilled(data);
                        } else {
                            listener.onError(task.getException());
                        }
                    }
                });
    }

    /* Meant to be used with getUserInfo ; pass userData into checkIfDataExists */
    public boolean checkIfDataExists(Map<String, Object> userData) {
        boolean exists = false;
        for (Map.Entry<String,Object> entry : userData.entrySet()){
            exists = true;
            break;
        }
        return exists;
    }

    public void registerUser(LoggedInUser userData, String securePassword, String salt, OnListener listener) {
        /* Format of userData:
        * name: name
        * phoneNumber: phoneNumber          // this is the user ID
        * password: Map<String, Object> {
        *               hash: hashed password
        *               salt: salt
        *           }
        * */
        String user = String.valueOf(userData.getUserId());
        this.getUserInfo(user, new OnListener() {
            @Override
            public void onFilled(Object result) {
                if (!checkIfDataExists((Map<String, Object>) result)) {
                    WriteBatch batch = db.batch();
                    CollectionReference userRef = getUserRef(user);
                    Log.i("loginF","registerUser: userRef: " + userRef.toString());

                    Map<String, Object> pwMap = new HashMap<>();
                    pwMap.put("hash",securePassword);
                    pwMap.put("salt",salt);

                    Map<String, Object> userMap = userData.getAllInfo();
                    userMap.put("password",pwMap);
                    Map<String, String> uselessMap = new HashMap<>();

                    batch.set(userRef.document("userInfo"),userMap);
                    batch.set(userRef.document("receiptsCollection"),uselessMap);
//                    batch.set(userRef.document("receiptsCollection").collection("createdReceipts"))
                    Log.i("loginF","registerUser: gonna batch.commit");
                    batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.i(TAG,"Managed to register user: " + user);
                            listener.onFilled(1);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "Failed to register user. Error: " + e.getMessage());
                            listener.onError(e);
                        }
                    });
                } else {
                    Log.i(TAG,"not supposed to happen");
                }
            }

            @Override
            public void onError(Exception taskException) {
                Log.i(TAG, "error??? " + taskException.getMessage());
            }
        });


    }

    public boolean verifyUser(Map<String, Object> userData, String providedPassword) {
        Map<String,String> passwordMap = (Map<String, String>) userData.get("password");
        String salt = passwordMap.get("salt");
        String hash = passwordMap.get("hash");
        return PasswordUtils.verifyUserPassword(providedPassword,hash,salt);
    }

    /*
    * Receipts methods
    */
    public void getAllCreatedReceipts(String user, OnListener listener) {
        this.getUserRef(user).document("receiptsCollection")
                .collection("createdReceipts").get()
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
/*Sample code on how to use getAllCreatedReceipts*/
/*
    fsm.getAllCreatedReceipts("[user id]", new FirestoreManager.OnListener() {
        @Override
        public void onFilled(Object result) {
            // do stuff with result
            Log.i(TAG,result.toString());
        }

        @Override
        public void onError(Exception taskException) {
            Log.i(TAG, "errorerror " + taskException);
        }
    });
*/

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
