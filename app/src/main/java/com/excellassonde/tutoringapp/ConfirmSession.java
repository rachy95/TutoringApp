package com.excellassonde.tutoringapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class ConfirmSession extends AppCompatActivity {

    private String currentSessionID;
    private SessionInformation session;
    DatabaseReference ref;
    DatabaseReference sessionsRef;
    String confirmationMessage;
    int currentSessionToConfirm = 0;
    boolean isConfirmed = false;
    boolean isRejected = false;

    public static Map<Integer, SessionInformation> sessionsToConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ref = FirebaseDatabase.getInstance().getReference();
        sessionsRef = ref.child(Utils.sessions);

        //set the confirmation message
        confirmationMessage = "Thank you. Your response has been received.";

        super.onCreate(savedInstanceState);
        if(sessionsToConfirm.isEmpty()){
            setContentView(R.layout.no_sessions_to_confirm);
        }

        else {
            setContentView(R.layout.activity_confirm_session);
            setConfirmSessionForm(sessionsToConfirm.get(currentSessionToConfirm));
        }


    }

    public void setConfirmSessionForm(SessionInformation session){
        //set the information
        currentSessionID = session.getSessionID();
        this.session = session;
        TextView studentName = (TextView) findViewById(R.id.student_name_edit);
        studentName.setText(session.getStudentName());
        TextView course = (TextView) findViewById(R.id.course_name_edit);
        course.setText(session.getCourse());
        TextView topics = (TextView) findViewById(R.id.topic_edit);
        topics.setText(session.getTopics());
        TextView startTime = (TextView) findViewById(R.id.start_session_edit);
        startTime.setText(session.getStartTime());
        TextView endTime = (TextView) findViewById(R.id.end_session_edit);
        endTime.setText(session.getEndTime());
        TextView sessionType = (TextView) findViewById(R.id.session_type_edit);
        sessionType.setText(session.getSessionType());
    }

    public void confirmSession(View view){
        Map<String, String> isConfirmedSession = new HashMap<>();
        isConfirmedSession.put("true", "");
        Map<String, Object> updateConfirmation = new HashMap<>();
        updateConfirmation.put("sessionResponse", isConfirmedSession);
        isConfirmed = true;
        String emails = TextUtils.join(",", session.getStudentEmails());
        sendEmail(emails, session.getTutorName(), session.getCourse(), session.getSessionDay(), session.getStartTime(), session.getEndTime());
        sessionsRef.child(currentSessionID).updateChildren(updateConfirmation);
        if(TutorMainPage.numberOfSessions > 1){
            currentSessionToConfirm++;
            setContentView(R.layout.activity_confirm_session);
            setConfirmSessionForm(sessionsToConfirm.get(currentSessionToConfirm));
        }
        else {
            Toast.makeText(ConfirmSession.this, confirmationMessage,
                    Toast.LENGTH_LONG).show();
            TutorMainPage.numberOfSessions--;
            Intent intent = new Intent(this, TutorMainPage.class);
            startActivity(intent);
        }
    }

    public void goBack(View view){
        Intent intent = new Intent(this, TutorMainPage.class);
        startActivity(intent);
    }

    public void rejectSession(View view){
        LayoutInflater factory = LayoutInflater.from(this);
        final View rejectionReason = factory.inflate(R.layout.reject_session_view, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(rejectionReason)
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        isRejected = false;
                        EditText reasonField = (EditText) rejectionReason.findViewById(R.id.rejection_reason);
                        String reason = reasonField.getText().toString();
                        String emails = TextUtils.join(",", session.getStudentEmails());
                        sendEmail(emails, session.getTutorName(), session.getCourse(), session.getSessionDay(), session.getStartTime(), session.getEndTime());
                        //store the reason
                        Map.Entry<Boolean, String> isConfirmed = new AbstractMap.SimpleEntry<>(false, reason);
                        Map<String, Object> updateConfirmation = new HashMap<>();
                        updateConfirmation.put("isSessionConfirmed", isConfirmed);
                        sessionsRef.child(currentSessionID).updateChildren(updateConfirmation);
                        //go back to the main page
                        Toast.makeText(ConfirmSession.this, "Thank you for your response!",
                                Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ConfirmSession.this, TutorMainPage.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                      dialog.cancel();
                    }
                });


    }

    public static void setSessionsToConfirm(Map<Integer, SessionInformation> sessions){
        sessionsToConfirm = new HashMap<>();
        sessionsToConfirm.putAll(sessions);
    }

    private void sendEmail(String studentEmail, String tutorName, String course, String day, String startTime, String endTime){
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");

        String subject = "Session Request for " + course;
        String body = "";


        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);

        if(isConfirmed){
            body = "Your session has been confirmed with "+tutorName + "for " +
                    "between "+ startTime + ":"+endTime+ ". Please meet them outside LAS 1006 in Lassonde building";
        }

        if(isRejected){
            body = "Unfortunately, " + tutorName + "could not confirm the session. We apologize for the inconvenience";
        }

        startActivity(Utils.sendEmail(studentEmail, subject, body));
    }

}
