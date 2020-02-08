package com.example.digitalreceipts.ui.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.digitalreceipts.Database.FirestoreManager;
import com.example.digitalreceipts.MainActivity.MainActivity;
import com.example.digitalreceipts.data.PasswordUtils;
import com.example.digitalreceipts.data.model.LoggedInUser;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digitalreceipts.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SignUpActivity extends AppCompatActivity {

    private Context context;
    private LoginViewModel logInViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirestoreManager fsm = new FirestoreManager();

        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        logInViewModel = ViewModelProviders.of(this,new LoginViewModelFactory()).get(LoginViewModel.class);
        logInViewModel.addClass(this);

        final EditText signUpName = findViewById(R.id.sign_up_name);
        final EditText signUpUser = findViewById(R.id.sign_up_user);
        final EditText signUpPassword = findViewById(R.id.sign_up_password);
        final TextView logInText = findViewById(R.id.log_in_text);
        final Button signUpButton = findViewById(R.id.sign_up_button);

        logInViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                signUpButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    signUpName.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    signUpPassword.setError(getString(loginFormState.getPasswordError()));
                }
                if(loginFormState.getNumberError() != null){
                    signUpUser.setError(getString(loginFormState.getNumberError()));
                }
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: implement creation of new user in database

                String password = signUpPassword.getText().toString();
                String salt = PasswordUtils.getSalt(8);
                String securePassword = PasswordUtils.generateSecurePassword(password, salt);

//                Map<String, Object> userData = new HashMap<>();
//                userData.put("name",signUpName.getText().toString());
//                userData.put("phoneNumber",Integer.parseInt(signupUser.getText().toString()));
//                userData.put("password",securePassword);
                LoggedInUser user = new LoggedInUser(signUpUser.getText().toString());
                user.setUserDetails(signUpUser.getText().toString(), signUpName.getText().toString());

                fsm.registerUser(user, securePassword, salt, new FirestoreManager.OnListener() {
                    @Override
                    public void onFilled(Object result) {
                        Log.i("hihi", "successful registration");
                        Intent intent = new Intent(context,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Exception taskException) {
                        Log.i("hihi", "smth is wrong");
                    }
                });
            }
        });


        logInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                logInViewModel.loginDataChanged(signUpName.getText().toString(),signUpPassword.getText().toString(),signUpUser.getText().toString());
            }
        };

        signUpName.addTextChangedListener(afterTextChangedListener);
        signUpUser.addTextChangedListener(afterTextChangedListener);
        signUpPassword.addTextChangedListener(afterTextChangedListener);

        // Firecloud Authentication protocols for Firecloud Notifications
        FirebaseMessaging.getInstance().subscribeToTopic("main")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FB", "cloud channel joining fialed", task.getException());
                        }

                    }
                });

    }
}
