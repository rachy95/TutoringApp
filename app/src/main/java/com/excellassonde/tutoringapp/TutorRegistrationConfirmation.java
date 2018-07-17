package com.excellassonde.tutoringapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.excellassonde.tutoringapp.R;

public class TutorRegistrationConfirmation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_registration_confirmation);
    }

    public void goToLoginPage(View view){
        AlertDialog alertDialog = new AlertDialog.Builder(TutorRegistrationConfirmation.this).create();
        alertDialog.setMessage("You'll be redirected to the Log in page. Please ensure that you have verified your account with the link sent to your email.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TutorRegistrationConfirmation.this, TutorLogin.class);
                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }
}
