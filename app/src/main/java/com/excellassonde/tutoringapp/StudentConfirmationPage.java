package com.excellassonde.tutoringapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class StudentConfirmationPage extends AppCompatActivity {
    String confirmMessage = "Your request has been received! A confirmation message will be sent once %s confirms this session";
    String totalConfirmMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_student_confirmation_page);
        //format the confirmation message with the tutor's name
        totalConfirmMessage = String.format(confirmMessage, SessionReview.tutorName);
        //set the message
        TextView confirm = (TextView)findViewById(R.id.confirm_message);
        confirm.setText(totalConfirmMessage);
        //store the sessions class
    }

    public void goToBeginningOfApp(View view){
        AlertDialog alertDialog = new AlertDialog.Builder(StudentConfirmationPage.this).create();
        alertDialog.setMessage("Would you like to book another session");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(StudentConfirmationPage.this, ChooseCourseActivity.class);
                        startActivity(intent);
                    }
                });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(StudentConfirmationPage.this, FirstActivity.class);
                        startActivity(intent);
                    }
                });
        alertDialog.show();
    }
}
