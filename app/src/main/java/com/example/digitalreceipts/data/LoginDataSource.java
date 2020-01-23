package com.example.digitalreceipts.data;

import android.util.Log;

import com.example.digitalreceipts.Database.FirestoreManager;
import com.example.digitalreceipts.data.model.LoggedInUser;

import java.io.IOException;
import java.util.Map;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public void login(String username, String providedpassword, Result.ResultListener resultListener) {

        try {
            // TODO: handle loggedInUser authentication
            FirestoreManager fsm = new FirestoreManager();
            fsm.getUserInfo(username, new FirestoreManager.OnListener() {
                @Override
                public void onFilled(Object result) {
                    Log.i("loginF","LoginDataSource result: " + result.toString());
                    Map<String, Object> userData = (Map<String, Object>) result;
                    if (!fsm.checkIfDataExists(userData)){
                        resultListener.onError(new Result.Error(new IOException("User does not exists")));
                    } else {
                        if(!fsm.verifyUser(userData,providedpassword)) {
                            resultListener.onError(new Result.Error(new IOException("Wrong password!")));
                        } else {
                            LoggedInUser newUser = new LoggedInUser(username);
                            resultListener.onSuccess(new Result.Success<LoggedInUser>(newUser));
                        }
                    }
                }

                @Override
                public void onError(Exception taskException) {
                    resultListener.onError(new Result.Error(new IOException("Unable to connect to Firestore!",taskException)));

                }
            });
//            LoggedInUser fakeUser =
//                    new LoggedInUser(
//                            java.util.UUID.randomUUID().toString(),
//                            "Jane Doe");
//            return new Result.Success<>(fakeUser);
//            return result;
        } catch (Exception e) {
            resultListener.onError(new Result.Error(new IOException("Error logging in", e)));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
