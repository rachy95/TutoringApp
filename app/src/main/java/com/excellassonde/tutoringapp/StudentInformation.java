package com.excellassonde.tutoringapp;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by praiseayorinde on 2017-09-23.
 */

//will be used as the database to store the information of the student and the sessions that has been booked
public class StudentInformation {
    private int id;
    private List<Integer> sessionIDs;
    private String studentName, studentEmail;

    public StudentInformation() {
        this.sessionIDs = new ArrayList<>();
        this.studentName = "";
        this.studentEmail = "";
        this.id = 0;
    }

    public int getId(){
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStudentName(){
        return this.studentName;
    }

    public void setStudentName(String name) {
        this.studentName = name;
    }

    public String getStudentEmail(){
        return this.studentEmail;
    }

    public void setStudentEmail(String email) {
        this.studentEmail = email;
    }

    public void addToSessions(int id){
        sessionIDs.add(id);
    }

    public int getNumberOfSessions(){
        return this.sessionIDs.size();
    }

    public String getSessionIDs() {
       //returns the session IDs that have been booked by the student
        return TextUtils.join(", ", sessionIDs);
    }
}
