package com.example.sudoku2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import android.view.View;
import android.widget.Button;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout usernameLayout, passwordLayout, confirmLayout;
    private TextInputEditText usernameInput, passwordInput, confirmInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameLayout = findViewById(R.id.usernameLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmLayout = findViewById(R.id.confirmLayout);
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmInput = findViewById(R.id.confirmInput);
        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnGoLogin = findViewById(R.id.btnGoLogin);

        btnRegister.setOnClickListener(this::validateAndRegister);
        btnGoLogin.setOnClickListener(v ->
                startActivity(new Intent(this, LoginActivity.class))
        );
    }

    private void validateAndRegister(View v) {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String confirm = confirmInput.getText().toString().trim();

        boolean valid = true;

        if (username.isEmpty()) {
            usernameLayout.setError(getString(R.string.enter_login_error));
            valid = false;
        } else {
            usernameLayout.setError(null);
        }

        if (password.isEmpty()) {
            passwordLayout.setError(getString(R.string.enter_password_error));
            valid = false;
        } else {
            passwordLayout.setError(null);
        }

        if (!password.equals(confirm)) {
            confirmLayout.setError(getString(R.string.passwords_match_error));
            valid = false;
        } else {
            confirmLayout.setError(null);
        }

        if (!valid) return;


        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit()
                .putString("username", username)
                .putString("password", password)
                .apply();

        Snackbar.make(v, getString(R.string.registration_successful), Snackbar.LENGTH_SHORT).show();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
