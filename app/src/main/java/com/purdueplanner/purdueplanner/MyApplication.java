package com.purdueplanner.purdueplanner;

import android.app.Application;

public class MyApplication extends Application {

    // Stores the application's current student
    private Student student;

    // Use to get the application's current student
    public Student getStudent()
    {
        return student;
    }

    // Use to set the application's current student
    public void setStudent(Student student)
    {
        this.student = student;
    }


}
