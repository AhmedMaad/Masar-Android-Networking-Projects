package com.maad.newsappversion5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerificationActivity extends AppCompatActivity {

    private String email;
    private String password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        setTitle("Verify Email");
        Toast.makeText(this, "Check your Email", Toast.LENGTH_SHORT).show();

        email = getIntent().getStringExtra("email");
        password = getIntent().getStringExtra("password");
        progressBar = findViewById(R.id.pb_verify_email);
    }

    public void verifyEmail(View view) {
        progressBar.setVisibility(View.VISIBLE);
        loginWithFirebase();
    }

    //Login existing user with firebase authentication
    public void loginWithFirebase() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        FirebaseUser user = task.getResult().getUser();
                        if (user.isEmailVerified()) {
                            progressBar.setVisibility(View.GONE);
                            //Go to home page
                            Intent intent = new Intent(EmailVerificationActivity.this
                                    , HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EmailVerificationActivity.this
                                    , "Check your Email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}