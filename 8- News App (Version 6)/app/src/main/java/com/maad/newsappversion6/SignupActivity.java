package com.maad.newsappversion6;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setTitle("Signup");
        mAuth = FirebaseAuth.getInstance();
    }

    public void openLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void register(View view) {

        EditText emailET = findViewById(R.id.email);
        EditText passwordET = findViewById(R.id.password);
        EditText confirmPasswordET = findViewById(R.id.conpassword);

        String email = emailET.getText().toString();
        String password = confirmPasswordET.getText().toString();
        String confirmPassword = passwordET.getText().toString();

        if (!password.equals(confirmPassword))
            Toast.makeText(this, "Passwords don't match", Toast.LENGTH_SHORT).show();
        else {
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Constants.USER_ID = task.getResult().getUser().getUid();
                                verifyEmail(email, password);
                            } else
                                Toast.makeText(SignupActivity.this
                                        , "Error: " + task.getException().getLocalizedMessage()
                                        , Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void verifyEmail(String email, String password) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(SignupActivity.this
                                        , EmailVerificationActivity.class);
                                i.putExtra("email", email);
                                i.putExtra("password", password);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
        }
    }

}