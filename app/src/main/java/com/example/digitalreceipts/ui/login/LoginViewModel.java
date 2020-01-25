package com.example.digitalreceipts.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.app.Activity;
import android.util.Log;

import com.example.digitalreceipts.data.LoginRepository;
import com.example.digitalreceipts.data.Result;
import com.example.digitalreceipts.data.model.LoggedInUser;
import com.example.digitalreceipts.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private Activity context;
    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }
    public void addClass(Activity context){
        this.context = context;
    }
    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        loginRepository.login(username, password, new Result.ResultListener() {

            @Override
            public void onSuccess(Result.Success result) {
                Log.i("loginF","LoginViewModel result: " + result.toString());
                if (result instanceof Result.Success) {
                    LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                    Log.i("loginF","login result name: " + data.getDisplayName());
                    loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                } else {
                    loginResult.setValue(new LoginResult(R.string.login_failed));
                }
            }

            @Override
            public void onError(Result.Error error) {
                Log.i("loginF","LoginViewModel result: " + error.toString());

                loginResult.setValue(new LoginResult(R.string.login_failed));
            }
        });


    }

    public void loginDataChanged(String username, String password, String number) {
        Log.wtf("this runs","observe");
        if (!isNameValid(username) ) {
            Log.wtf("this runs invalid name","observe");
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null, null));

        } else if (!isUserNameValid(number)&& context instanceof SignUpActivity){
            Log.wtf("this runsinvalid username", "observe");
            loginFormState.setValue(new LoginFormState(null, null, R.string.invalid_number));

        } else if (!isPasswordValid(password)) {
            Log.wtf("this runs invalid password", "observe");
                loginFormState.setValue(new LoginFormState(null, R.string.invalid_password, null));

        }  else{
            Log.wtf("this runs everything works","observe");
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder name validation check
    private boolean isNameValid(String name) {
        return (name != null && name.trim().length() >5);
    }
    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    //A placeholder username validation check
    private boolean isUserNameValid(String username){
        //TODO Le Xuan can you add a check if there is an existing username in base
        return username!=null && username.trim().length()>5;
    }
}
