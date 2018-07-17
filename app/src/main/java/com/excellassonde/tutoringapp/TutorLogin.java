package com.excellassonde.tutoringapp;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class TutorLogin extends AppCompatActivity {
    private static final String TAG = "EmailPassword";
    private FirebaseAuth auth;
    DatabaseReference ref;
    DatabaseReference sessionsRef;

    EditText emailField;
    EditText passwordField;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_login);
        auth = FirebaseAuth.getInstance();
        emailField = (EditText) findViewById(R.id.tutor_login_email);
        passwordField = (EditText) findViewById(R.id.tutor_login_password);

    }

    public void login(View view){
        signIn(emailField.getText().toString(), passwordField.getText().toString());
    }

    private void signIn(String email, String password) {
        if (!validateForm()) {
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //we need to make sure that their email has been verified
                            if(auth.getCurrentUser() != null) {
                                auth.getCurrentUser().reload();
                            }

                            FirebaseUser user = auth.getCurrentUser();
                            if(user != null && user.isEmailVerified()) {
                                setMainPage();
                                Intent intent = new Intent(TutorLogin.this, TutorMainPage.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(TutorLogin.this, "please verify your account first.", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(TutorLogin.this, " Authentication failed. - "+task.getResult(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }



    private boolean validateForm() {
        boolean valid = true;

        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
            valid = false;
        } else {
            emailField.setError(null);
        }
//
//        if(Utils.isValidEmail(email)){
//            emailField.setError("Enter valid Yorku Email.");
//            valid = false;
//        } else {
//            emailField.setError(null);
//        }

        String password = passwordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            passwordField.setError("Required.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    public void forgotPassword(View view){
        String email = emailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            emailField.setError("Required.");
        }
        forgotPasswordSendEmail(email);
    }

    private void forgotPasswordSendEmail(String email){
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(TutorLogin.this, "Email sent.", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    public void loginPageSignUp(View view){
        Intent intent = new Intent(TutorLogin.this, TutorSignUp.class);
        startActivity(intent);
    }

    public void appendYorkUEmail(View view){
        emailField.setText(R.string.yorku_email_ending);
    }


    private void setMainPage(){
        //we need to check if there are sessions to confirm

        final Map<Integer, SessionInformation> sessionsToConfirm = new HashMap<>();

        ref = FirebaseDatabase.getInstance().getReference();
        sessionsRef = FirebaseDatabase.getInstance().getReference().child(Utils.sessions);
        sessionsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                int counter = 0;
                if (dataSnapshot.getValue() != null) {
                    // String key = String.valueOf(dataSnapshot.getValue());
                    SessionInformation session = dataSnapshot.getValue(SessionInformation.class);
                    boolean isSessionConfirmed = session.isSessionConfirmed();
                    boolean isSessionRejected = session.isSessionRejected();
                    //the session has to be this particular tutors
                    //also, we have to check a response has not been given yet
                    boolean equal = session.getTutorName().equals(Utils.getTutorName());
                    if ( equal && !isSessionConfirmed && !isSessionRejected) {
                        sessionsToConfirm.put(counter, session);
                        ConfirmSession.setSessionsToConfirm(sessionsToConfirm);
                        TutorMainPage.numberOfSessions++;
                        counter++;
                    }
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null){
                    int counter = 0;
                    Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                    for (DataSnapshot child : children) {
                        SessionInformation session = child.getValue(SessionInformation.class);
                        boolean isSessionConfirmed = session.isSessionConfirmed();
                        boolean isSessionRejected = session.isSessionRejected();
                        //the session has to be this particular tutors
                        //also, we have to check a response has not been given yet
                        if(session.getTutorName().equals(Utils.getTutorName()) && !isSessionConfirmed && isSessionRejected ) {
                            sessionsToConfirm.put(counter, session);
                            TutorMainPage.numberOfSessions++;
                            counter++;
                        }
                    }
                }
                ConfirmSession.setSessionsToConfirm(sessionsToConfirm);
                Intent intent = new Intent(TutorLogin.this, TutorMainPage.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        DatabaseReference tutorRef = FirebaseDatabase.getInstance().getReference().child(Utils.tutors).child(Utils.getTutorName());
        tutorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TutorInformation tutorInformation = dataSnapshot.getValue(TutorInformation.class);
                try {
                    Utils.setIsTranscriptSubmitted(tutorInformation.isTranscriptSubmitted());
                }
                catch (NullPointerException e){
                    //the tutor does not exist anymore
                    Utils.setIsTranscriptSubmitted(false);
                }
                Intent intent = new Intent(TutorLogin.this, TutorMainPage.class);
                startActivity(intent);
                AdjustAvailability.setTutorInformation(tutorInformation);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });


    }
}
