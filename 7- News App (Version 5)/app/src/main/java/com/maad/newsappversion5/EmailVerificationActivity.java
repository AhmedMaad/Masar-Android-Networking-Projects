package com.maad.newsappversion5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.miniolx.R;
import com.example.miniolx.data.Util;
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

        Toast.makeText(this, R.string.verify_email, Toast.LENGTH_SHORT).show();

        email = getIntent().getStringExtra("Email");
        password = getIntent().getStringExtra("Password");
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
                        Log.d("json", "Email found: " + user.getEmail());
                        if (user.isEmailVerified()) {
                            Util.U_ID = user.getUid();
                            //Go to home page
                            Intent intent = new Intent(EmailVerificationActivity.this
                                    , HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(EmailVerificationActivity.this
                                    , R.string.verify_email, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}