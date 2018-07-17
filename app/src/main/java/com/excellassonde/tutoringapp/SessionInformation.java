package com.excellassonde.tutoringapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the format for storing  a session that has been requested by the student.
 * If the student is confirmed by the tutor, we can know and then send a reply to the student
 */
@IgnoreExtraProperties
public class SessionInformation {
    private String sessionID;
    private int sessionScore;
    private String tutorName;
    private String course;
    private String sessionDay;
    private String startTime;
    private String endTime;
    private String sessionType;
    private List<String> studentEmails;
    private String studentName;
    private Map<String, String> sessionResponse;
    private String topics;

    public SessionInformation() {
        this.sessionID = "";
        this.sessionScore = 0;
        this.tutorName = "";
        this.course = "";
        this.sessionDay = "";
        this.startTime = "";
        this.endTime = "";
        this.sessionType = Utils.SessionType.ONE_ON_ONE.toString();
        this.studentEmails = new ArrayList<>();
        this.studentName = "";
        this.sessionResponse = new HashMap<>();
        this.topics = "";
    }

    public SessionInformation(String id, String tName, String course, String day, String start, String end, String type, List<String> emails, String sName, String topics) {
        this.sessionID = id;
        this.sessionScore = 0;
        this.tutorName = tName;
        this.course = course;
        this.sessionDay = day;
        this.startTime = start;
        this.endTime = end;
        this.sessionType = type;
        this.studentEmails = emails;
        this.studentName = sName;
        this.sessionResponse = new HashMap<>();
        this.sessionResponse.put("false", "");
        this.topics = topics;
    }
    public String getSessionID() {
        return this.sessionID;
    }

    public void setSessionID(String id) {
        this.sessionID = id;
    }

    public int getSessionScore() {
        return sessionScore;
    }

    public void setSessionScore(int score) {
        this.sessionScore = score;
    }

    public String getTutorName() {
        return tutorName;
    }

    public void setTutorName(String name) {
        this.tutorName = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSessionDay() {
        return sessionDay.toString();
    }

    public void setSessionDay(String day) {
        this.sessionDay = day;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String time) {
        this.startTime = time;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String time) {
        this.endTime = time;
    }

    public String getSessionType() {
        return this.sessionType;
    }

    public void setSessionType(String type) {
        this.sessionType = type;
    }

    public void setStudentEmails(List<String> emails) {
        this.studentEmails.addAll(emails);
    }

    @Exclude
    public void addToStudentEmails(String email) {
        this.studentEmails.add(email);
    }

    @Exclude
    public void addToStudentEmails(List emails) {
        this.studentEmails.addAll(emails);
    }

    public List<String> getStudentEmails() {
        return this.studentEmails;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public void setStudentName(String name) {
        this.studentName = name;
    }

    public void setSessionResponse(Map<String, String> response) {
        this.sessionResponse = response;
    }

    public Map<String, String> getSessionResponse() {
        return this.sessionResponse;
    }

    //the student just has to know if the session has been confirmed or not, they do not have to know the reason
    @Exclude
    public boolean isSessionConfirmed() {
        return this.sessionResponse.containsKey("true");
    }

    //this is not just the opposite of if the session is confirmed.
    //if the session has not been given any response then the key will be false too
    //but if the session has been rejected then thw value will not be empty
    @Exclude
    public boolean isSessionRejected() {
       return this.sessionResponse.containsKey("false") && !this.sessionResponse.get("false").isEmpty();
    }

    public void setTopics(String topic) {
        this.topics = topic;
    }

    public String getTopics() {
        return this.topics;
    }

}


