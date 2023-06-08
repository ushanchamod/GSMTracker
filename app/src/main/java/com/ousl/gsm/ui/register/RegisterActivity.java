package com.ousl.gsm.ui.register;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.ousl.gsm.DatabaseHelper;
import com.ousl.gsm.R;
import com.ousl.gsm.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText etUsername, etPasswordOne, etPasswordTwo;
    DatabaseHelper myDb;
    Button btnRegister;
    TextView loginLink;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loginLink = findViewById(R.id.textViewLogin);
        btnRegister = findViewById(R.id.reg_button);

        etUsername = findViewById(R.id.editTextUsername);
        etPasswordOne = findViewById(R.id.editTextPassword);
        etPasswordTwo = findViewById(R.id.editTextConfirmPassword);

        loginLinkClick(); handleSignUp();
    }

    // handle sign up button click
    public void handleSignUp(){
        btnRegister.setOnClickListener(v -> {

            // check all feldspar are filled
            if(etUsername.getText().toString().equals("")){
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.red));
                etUsername.setBackgroundTintList(colorStateList);
                etUsername.setTextColor(colorStateList);

                showToast("Please fill username fields");
                return;
            }
            else{
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.black));
                etUsername.setBackgroundTintList(colorStateList);
                etUsername.setTextColor(colorStateList);
            }

            if(etPasswordOne.getText().toString().equals("")){
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.red));
                etPasswordOne.setBackgroundTintList(colorStateList);
                etPasswordOne.setTextColor(colorStateList);

                showToast("Please password fields");
                return;
            }
            else{
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.black));
                etPasswordOne.setBackgroundTintList(colorStateList);
                etPasswordOne.setTextColor(colorStateList);
            }

            if(etPasswordTwo.getText().toString().equals("")){
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.red));
                etPasswordTwo.setBackgroundTintList(colorStateList);
                etPasswordTwo.setTextColor(colorStateList);

                Toast.makeText(RegisterActivity.this, "Please fill confirm password fields", Toast.LENGTH_SHORT).show();
                return;
            }
            else{
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.black));
                etPasswordTwo.setBackgroundTintList(colorStateList);
                etPasswordTwo.setTextColor(colorStateList);
            }

            if(etPasswordOne.getText().toString().length() < 5){
                showToast("Password should be at least 5 characters");
                return;
            }


            // check if passwords match
            if(!etPasswordOne.getText().toString().equals(etPasswordTwo.getText().toString())){
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.black));
                etPasswordTwo.setBackgroundTintList(colorStateList);
                etPasswordTwo.setTextColor(colorStateList);

                etPasswordOne.setBackgroundTintList(colorStateList);
                etPasswordOne.setTextColor(colorStateList);


                showToast("Passwords do not match");
                return;
            }
            else{
                ColorStateList colorStateList = ColorStateList.valueOf(ContextCompat.getColor(RegisterActivity.this, R.color.black));
                etPasswordOne.setBackgroundTintList(colorStateList);
                etPasswordOne.setTextColor(colorStateList);

                etPasswordTwo.setBackgroundTintList(colorStateList);
                etPasswordTwo.setTextColor(colorStateList);
            }

            // check if username already exists
            if(!checkUsername(etUsername.getText().toString())){
                showToast("Username already exists");
                return;
            }

            // add user to database
            myDb = new DatabaseHelper(RegisterActivity.this);
            boolean isInserted = myDb.insertDataToUser(etUsername.getText().toString(), etPasswordOne.getText().toString());
            if(isInserted){
                showToast("User Registered Successfully");
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }else{
                showToast("User Registration Failed");
            }
        });
    }

    public void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    } // show t

    public boolean checkUsername(String username){
        // check if username already exists
        myDb = new DatabaseHelper(this);
        Cursor res = myDb.getAllDataFromUser(username);
        while (res.moveToNext()){
            if(res.getString(1).equals(username)){
                return false;
            }
        }
        return true;

    }

    public void loginLinkClick(){
        loginLink.setOnClickListener(v -> {
            Intent intent = new Intent( RegisterActivity.this , LoginActivity.class);
            startActivity(intent);
        });
    }
}