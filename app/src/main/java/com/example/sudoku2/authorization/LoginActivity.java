package com.example.sudoku2.authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.sudoku2.MainActivity;
import com.example.sudoku2.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private TextInputLayout usernameLayout, passwordLayout;
    private TextInputEditText usernameInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoRegister = findViewById(R.id.btnGoRegister);

        btnLogin.setOnClickListener(v -> validateAndLogin(v));
        btnGoRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));

    }

    private void validateAndLogin(View v){
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        boolean valid = true;

        if (username.isEmpty()){
            usernameLayout.setError(getString(R.string.enter_login_error));
            valid = false;
        }
        else{
            usernameLayout.setError(null);
        }

        if (password.isEmpty()){
            passwordLayout.setError(getString(R.string.enter_password_error));
            valid = false;
        }
        else {
            passwordLayout.setError(null);
        }

        if (!valid) return;
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String savedUser = prefs.getString("username", null);
        String savedPass = prefs.getString("password", null);
        if (username.equals(savedUser) && password.equals(savedPass)){
            prefs.edit().putBoolean("loggedIn", true).apply();
            Snackbar.make(v, getString(R.string.welcome), Snackbar.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else {
            Snackbar.make(v, getString(R.string.incorrect_login_or_password), Snackbar.LENGTH_SHORT).show();
        }
    }
}
