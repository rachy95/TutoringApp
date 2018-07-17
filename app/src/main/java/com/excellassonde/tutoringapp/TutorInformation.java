package com.excellassonde.tutoringapp;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Define the tutor class
 */
@IgnoreExtraProperties
public class TutorInformation {
    private String name;
    private double availableHours;
    private List<String> courses;
    private String phoneNumber;
    private Map<String, Map<String, Boolean>> availability;
    private double hoursBooked;
    private String email;
    private String tutorID;
    private boolean transcriptSubmitted;

    public TutorInformation() {
        this.name = "";
        this.tutorID = "";
        this.availableHours = 0.0;
        this.courses = new ArrayList<>();
        this.phoneNumber = "";
        this.availability = new HashMap<>();
        this.hoursBooked = 0.0;
        this.email = "";
        transcriptSubmitted = false;
    }

    /***
     * create a tutor with a name, a list of courses they teach and their availabilities
     * @param aName the full name of a tutor
     * @param anEmail the email addy of a tutor
     * @param aPhoneNumber the phone number of the tutor
     * @param listCourses A list of courses the tutor is willing to teach
     * @param availableHours the hours a tutor is willing to tutor for3
     * @param avails the times and days a tutor is available.
     *               the day is the key, and a list is the times the tutor is free.
     *                       Each list is an entry with the time they are free with if it has been booked
     */
    public TutorInformation(String aName, String studentNo, String anEmail, String aPhoneNumber, List<String> listCourses, double availableHours, Map<String, Map<String, Boolean>> avails) {
        this.name = aName;
        this.tutorID = studentNo;
        this.phoneNumber = aPhoneNumber;
        this.courses = listCourses;
        this.availability = avails;
        this.availableHours = availableHours;
        this.email = anEmail;
        this.hoursBooked = 0;
        this.transcriptSubmitted = false;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String aname) {
        this.name = aname;
    }

    public double getAvailableHours() {
        return this.availableHours;
    }

    public void setAvailableHours(double availableHours) {
        this.availableHours = availableHours;
    }

    public List<String> getCourses() {
        return this.courses;
    }

    public void setCourses(List<String> courses) {
        this.courses.addAll(courses);
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Map<String, Map<String, Boolean>> getAvailability() {
        return this.availability;
    }

    public void setAvailability(Map<String, Map<String, Boolean>> availability) {
        this.availability = availability;
    }

    public double getHoursBooked() {
        return this.hoursBooked;
    }

    public void setHoursBooked(double hoursBooked) {
        this.hoursBooked = hoursBooked;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTutorID() {
        return this.tutorID;
    }

    public void setTutorID(String tutorID) {
        this.tutorID = tutorID;
    }

    public boolean isTranscriptSubmitted() {
        return transcriptSubmitted;
    }

    public void setTranscriptSubmitted(boolean isTranscriptSubmitted) {
        this.transcriptSubmitted = isTranscriptSubmitted;
    }

    /***
     *
     * @return a set of days this tutor is available
     */
    @Exclude
    public Set<String> getDaysAvailable() {
        return availability.keySet();
    }

    /***
     *
     * @param day the day this tutor might be available
     * @return the times, given a particular day a tutor teaches, if the tutor is not available on the given day,
     * an empty list is returned
     */
    @Exclude
    public Map<String, Boolean> getTimes(String day) {

        if (getDaysAvailable().contains(day)) {
            return availability.get(day);
        } else {
            return new HashMap<>();
        }
    }

    /***
     *
     * @param course the course a tutor might teach
     * @return if this tutor teaches a particular course called 'course'
     */
    @Exclude
    public boolean teachCourse(String course) {
        return courses.contains(course);
    }

    /***
     *
     * @param hours the amount of hours the tutor has been booked for
     */
    @Exclude
    public void addToHoursBooked(double hours) {
        hoursBooked += hours;
    }

    //given a new availability, reset it to be this value
    @Exclude
    public void resetAvailability(String day, Map<String, Boolean> times) {
        availability.put(day, times);
    }

    /***
     *
     * @return if the tutor has reached the available hours he is willing to tutor for
     */
    @Exclude
    public boolean reachedMaximumHours() {
        return this.hoursBooked >= this.availableHours;
    }

}
