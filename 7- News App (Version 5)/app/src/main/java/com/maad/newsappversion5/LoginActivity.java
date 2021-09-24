package com.maad.newsappversion5;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//TODO: Add your own "google-services.json" file under Android app module root directory.
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

    public void sendResetPasswordLink(View view) {
        LayoutInflater inflater = getLayoutInflater();
        // Pass null as the parent view because its going in the dialog layout
        View v = inflater.inflate(R.layout.password_alert_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder
                .setView(v)
                .setPositiveButton("Send", (dialog, which) -> {
                    EditText emailET = v.findViewById(R.id.et_email);
                    String emailAddress = emailET.getText().toString();
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(LoginActivity.this
                                                , "Check your email", Toast.LENGTH_SHORT).show();
                                    } else
                                        Toast.makeText(LoginActivity.this
                                                , "No account is registered with this email"
                                                , Toast.LENGTH_SHORT).show();
                                }
                            });
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dialog.dismiss();
                })
                .setCancelable(false)
                .show();
    }

}