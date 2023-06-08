package com.ousl.gsm.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ousl.gsm.DatabaseHelper;
import com.ousl.gsm.MainActivity;
import com.ousl.gsm.R;
import com.ousl.gsm.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    EditText etUsername, etPassword;
    TextView registerLink;
    DatabaseHelper myDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myDb = new DatabaseHelper(this);

        btnLogin = findViewById(R.id.loging_button);
        etUsername = findViewById(R.id.editTextUserName);
        etPassword = findViewById(R.id.editTextPassword);

        handleLoggingBtnPress();

        registerLink = findViewById(R.id.textViewSignupLink);
        launchRegisterActivity();
    }

    public void launchRegisterActivity(){
        registerLink.setOnClickListener(v -> {
            Intent intent = new Intent( LoginActivity.this , RegisterActivity.class);
            startActivity(intent);
        });
    }

    public void handleLoggingBtnPress(){
        btnLogin.setOnClickListener(v -> {
            if(etUsername.getText().toString().trim().length() == 0){
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.red));
                etUsername.setBackgroundTintList(colorStateList);

                showToast("Please Enter Username");
                return;
            }
            else{
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.black));
                etUsername.setBackgroundTintList(colorStateList);
            }

            if(etPassword.getText().toString().trim().length() == 0){
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.red));
                etUsername.setBackgroundTintList(colorStateList);
                etUsername.setTextColor(colorStateList);

                showToast("Please Enter Password");
                return;
            }
            else{
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.black));
                etUsername.setBackgroundTintList(colorStateList);
                etUsername.setTextColor(colorStateList);
            }

            // check if the username and password are correct
            Cursor results = myDb.getAllDataFromUser(etUsername.getText().toString());

            if(results.getCount() == 0){
                return;
            }

            String id = results.moveToFirst() ? results.getString(0) : null;
            String username = results.moveToFirst() ? results.getString(1) : null;
            String password = results.moveToFirst() ? results.getString(2) : null;


            // check if the username and password are correct
            if (etUsername.getText().toString().equals(username) &&
                    etPassword.getText().toString().equals(password)) {

                // set the isAuthenticated flag to true and save it in SharedPreferences
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("is_authenticated", true);
                editor.putString("user_id", id);
                editor.putString("user_name", username);
                editor.apply();


                // launch MainActivity and finish the LoginActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                showToast("You have successfully login");
                finish();
            } else {
                // show an error message
                showToast("Invalid username or password");
                etUsername.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.red));
                etUsername.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.red)));

                etPassword.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.red));
                etPassword.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(LoginActivity.this, R.color.red)));
            }
        });
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    } // show toast message


}