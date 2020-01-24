package com.example.digitalreceipts.data;

import android.util.Log;

import com.example.digitalreceipts.data.model.LoggedInUser;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private LoginDataSource dataSource;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    private LoginRepository(LoginDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static LoginRepository getInstance(LoginDataSource dataSource) {
        if (instance == null) {
            instance = new LoginRepository(dataSource);
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }

    private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    public void login(String username, String password, Result.ResultListener resultListener) {
        // handle login
        dataSource.login(username, password, new Result.ResultListener() {

            @Override
            public void onSuccess(Result.Success result) {
                Log.i("loginF","LoginRepository result: " + result.toString());
                if (result instanceof  Result.Success) {
                    setLoggedInUser(((Result.Success<LoggedInUser>) result).getData());
                    resultListener.onSuccess(result);
                } else {
                    resultListener.onError(new Result.Error(new Exception("idk what's wrong tbh")));
                }
            }

            @Override
            public void onError(Result.Error error) {
                Log.i("loginF","LoginRepository result: " + error.toString());

                resultListener.onError(error);
            }
        });
    }
}
