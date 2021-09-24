package com.maad.newsappversion5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
    }

    public void openSignupActivity(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }

    public void login(View view) {

        EditText emailET = findViewById(R.id.email);
        EditText passwordET = findViewById(R.id.password);

        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (task.isSuccessful() && user.isEmailVerified()) {
                            //navigate to main activity
                            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(i);
                            finish();
                        } else if (task.isSuccessful() && !user.isEmailVerified()) {
                            Intent i = new Intent(LoginActivity.this
                                    , EmailVerificationActivity.class);
                            i.putExtra("email", email);
                            i.putExtra("password", password);
                            startActivity(i);
                            finish();
                        } else
                            Toast.makeText(LoginActivity.this, "Wrong email or password"
                                    , Toast.LENGTH_SHORT).show();
                    }
                });
    }

}