package com.duburchallenge.duburchallenge;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout tilEmail, tilPwd, tilUsername;
    private EditText edtEmail, edtPwd, edtUsername;
    private Button btnSignUp;
    private boolean isValidPwd, isValidEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

                tilUsername = findViewById(R.id.til_username);
                tilEmail = findViewById(R.id.til_email);
                tilPwd = findViewById(R.id.til_pwd);
                edtUsername = findViewById(R.id.username);
                edtEmail = findViewById(R.id.email);
                edtPwd = findViewById(R.id.password);
                btnSignUp = findViewById(R.id.btnSignup);

        edtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidEmail = true;
                String email = s.toString();
                String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
                if (email.equalsIgnoreCase("")) {
                    tilEmail.setError("Please, enter the email address");
                    isValidEmail = false;
                    tilEmail.setErrorEnabled(true);
                } else if (!email.matches(emailPattern)) {
                    tilEmail.setError("Invalid email address");
                    isValidEmail = false;
                    tilEmail.setErrorEnabled(true);
                } else {
                    tilEmail.setError("");
                    tilEmail.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        edtPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValidPwd = true;
                String password = s.toString();
                String passwordPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
                if (password.equalsIgnoreCase("")) {
                    tilPwd.setError("Please, Enter the password");
                    isValidPwd = false;
                    tilPwd.setErrorEnabled(true);
                } else if (!password.matches(passwordPattern)) {
                    tilPwd.setError("Password must be 8 character long with combination of number,capital and small letter with special symbol)");
                    isValidPwd = false;
                    tilPwd.setErrorEnabled(true);
                } else {
                    tilPwd.setError("");
                    tilPwd.setErrorEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnSignup) {

            if (isValidEmail == true && isValidPwd == true) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
                intent.putExtra("email",edtEmail.getText().toString());
                intent.putExtra("password",edtPwd.getText().toString());
                startActivity(intent);
            }
        }
    }

}
