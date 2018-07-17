package com.excellassonde.tutoringapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class TutorSignUp extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authStateListener;

    EditText nameField;
    EditText emailField;
    EditText passwordField;
    EditText passwordTwoField;

    public static String tutorName;
    public static String tutorEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_sign_up);
        auth = FirebaseAuth.getInstance();

        nameField = (EditText) findViewById(R.id.full_name);
        emailField = (EditText) findViewById(R.id.tutor_signup_email);
        passwordField = (EditText) findViewById(R.id.tutor_signup_password);
        passwordTwoField = (EditText) findViewById(R.id.tutor_signup_password_confirm);
    }

    public void signUp(View view){
        tutorName = nameField.getText().toString();
        tutorEmail = emailField.getText().toString();
        String password = passwordField.getText().toString();
        createAccount(tutorName, tutorEmail, password);

    }


    private void createAccount(final String fullName, final String email, final String password) {
//        if (!validateForm()) {
//            return;
//        }

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(TutorSignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(TutorSignUp.this, TutorStepToStepRegistration.class);
                            // Sign up success
                            FirebaseUser user = auth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(fullName).build();
                            user.updateProfile(profileUpdates);
                            sendEmailVerification(user);
                            startActivity(intent);

                        }

                        else{
                            //the person already has an account
                            Toast.makeText(TutorSignUp.this, task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }


    private void sendEmailVerification(final FirebaseUser user) {
        // Send verification email
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(TutorSignUp.this,
                                    "Verification email sent to " + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TutorSignUp.this,
                                    "Failed to send verification email.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        //1. Email cannot be empty
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        //2. Email has to be a york email so that we make sure they are students
        if(!email.endsWith("my.yorku.ca")){
            emailField.setError("Enter a valid yorku email.");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString();
        String passwordConfirm = passwordTwoField.getText().toString();


        //3. the password cannot be empty
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        //4. Password cannot be less than 6 characters - a requirement from firebase
        if(password.length() < 6){
            passwordField.setError("Enter at least 6 characters.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        //4. the passwords have to match
        if(!password.equals(passwordConfirm)){
            passwordTwoField.setError("Passwords do not match.");
            valid = false;
        }



        return valid;
    }

    public void signUpPageLogIn(View view){
        Intent intent = new Intent(TutorSignUp.this, TutorLogin.class);
        startActivity(intent);
    }
}
