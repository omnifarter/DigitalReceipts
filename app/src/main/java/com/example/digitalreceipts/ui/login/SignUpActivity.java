package com.example.digitalreceipts.ui.login;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import com.example.digitalreceipts.ui.login.LoginViewModel;
import com.example.digitalreceipts.ui.login.LoginViewModelFactory;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.digitalreceipts.R;

public class SignUpActivity extends AppCompatActivity {

    private Context context;
    private LoginViewModel logInViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        logInViewModel = ViewModelProviders.of(this,new LoginViewModelFactory()).get(LoginViewModel.class);
        logInViewModel.addClass(this);

        final EditText signUpName = findViewById(R.id.sign_up_name);
        final EditText signUpNumber = findViewById(R.id.sign_up_number);
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
                    signUpNumber.setError(getString(loginFormState.getNumberError()));
                }
            }
        });
        logInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: implement creation of new user in database
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
                logInViewModel.loginDataChanged(signUpName.getText().toString(),signUpPassword.getText().toString(),signUpNumber.getText().toString());
            }
        };

        signUpName.addTextChangedListener(afterTextChangedListener);
        signUpNumber.addTextChangedListener(afterTextChangedListener);
        signUpPassword.addTextChangedListener(afterTextChangedListener);

    }
}
