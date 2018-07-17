package com.excellassonde.tutoringapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
    }

    public void goToTutorLogin(View view){
        if(Utils.getTutorName() == null || Utils.getTutorName().isEmpty()) {
            Intent intent = new Intent(this, TutorLogin.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, TutorMainPage.class);
            startActivity(intent);
        }
    }

    public void goToStudentLogin(View view){
        Intent intent = new Intent(this, StudentLogin.class);
        startActivity(intent);
    }
}
