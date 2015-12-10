package com.purdueplanner.purdueplanner;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by michaelrollberg on 11/9/15.
 */
public class Student {

    // Stores the student's unique identification number
    private String id;
    // Stores the student's first name
    private String firstName;
    // Stores the student's last name
    private String lastName;
    // Stores the student's schedule of classes
    private ArrayList<Classes> schedule;
    // Stores the student's Latitude
    private double Latitude;
    // Stores the student's Longitude
    private double Longitude;
    // Stores the whether or not the student wants to hide their schedule from friends
    private boolean isScheduleHidden;


    // Declare an empty student
    public Student()
    {

    }

    // Declare a complete student
    public Student(String id, String firstName, String lastName, ArrayList<Classes> schedule, double Latitude, double Longitude, boolean isScheduleHidden)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.schedule = schedule;
        this.Latitude = Latitude;
        this.Longitude = Longitude;
        this.isScheduleHidden = isScheduleHidden;

    }

    // Get the student's unique identification number
    public String getId()
    {
        return id;
    }

    // Set the student's unique identification number
    public void setId(String id)
    {
        this.id = id;
    }

    // Get the student's first name
    public String getFirstName() {
        return firstName;
    }

    // Set the student's first name
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    // Get the student's last name
    public String getLastName()
    {
        return lastName;
    }

    // Set the student's last name
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    // Get the student's schedule of classes
    public ArrayList<Classes> getSchedule()
    {
        return schedule;
    }

    // Set the student's schedule of classes
    public void setSchedule(ArrayList<Classes> schedule)
    {
        this.schedule = schedule;
    }

    // Set the student's Latitude
    public void setLatitude(double Latitude) { this.Latitude = Latitude;}

    // Set the student's Latitude
    public void setLongitude(double Longitude) { this.Longitude = Longitude;}

    // Get the student's Latitude
    public double getLatitude() { return Latitude;}

    // Get the student's Longitude
    public double getLongitude() { return Longitude;}

    // Set whether or not the student wants to hide their schedule from friends
    public void setScheduleHidden(boolean isScheduleHidden)
    {
        this.isScheduleHidden = isScheduleHidden;
    }

    // Get whether or not the student wants to hide their schedule from friends
    public boolean getScheduleHidden()
    {
        return isScheduleHidden;
    }

}
