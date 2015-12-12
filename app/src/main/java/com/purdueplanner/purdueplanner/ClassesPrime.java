package com.purdueplanner.purdueplanner;

/**
 * Created by Kenny on 11/8/2015.
 */
public class ClassesPrime {
    // Stores the class's start time
    private String startTime;
    // Stores the class's end time
    private String endTime;
    // Stores the class's start date
    private String startDate;
    // Stores the class's end date
    private String endDate;
    // Stores the class's CRN
    private String CRN;
    // Stores the class's major
    private String major;
    // Stores the class's course number
    private String courseNum;
    // Stores the class's section number
    private String sectionNum;
    // Stores the class's credit number
    private String credits;
    // Stores the class's title
    private String title;
    // Stores the days the class meets
    private String days;
    // Stores the primary instructor of the class
    private String instructor;
    // Stores the primary instructor's email
    private String instructorEmail;
    // Stores the location the class meets at
    private String location;
    // Stores the type of class
    private String type;
    // Stores the latitude
    private String latitude;
    // Stores the longitude
    private String longitude;


    /*public ClassesPrime() {*/
    public ClassesPrime(String startTime, String endTime, String startDate, String endDate,
                        String CRN, String major, String courseNum, String section, String credits,
                        String title, String days, String instructors, String instructorEmail, String location,
                        String type, String latitude, String longitude)
    {
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.CRN = CRN;
        this.major = major;
        this.courseNum = courseNum;
        this.sectionNum= section;
        this.credits = credits;
        this.title = title;
        this.days = days;
        this.instructor = instructors;
        this.instructorEmail = instructorEmail;
        this.location = location;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;

    }
    public String getStartTime() {
        return startTime;
    }
    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getCRN() {
        return CRN;
    }
    public void setCRN(String cRN) {
        CRN = cRN;
    }
    public String getMajor() {
        return major;
    }
    public void setMajor(String major) {
        this.major = major;
    }
    public String getCourseNum() {
        return courseNum;
    }
    public void setCourseNum(String courseNum) {
        this.courseNum = courseNum;
    }
    public String getSectionNum() {
        return sectionNum;
    }
    public void setSectionNum(String sectionNum) {
        this.sectionNum = sectionNum;
    }
    public String getCredits() {
        return credits;
    }
    public void setCredits(String string) {
        this.credits = string;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDays() {
        return days;
    }
    public void setDays(String days) {
        this.days = days;
    }
    public String getInstructor() {
        return instructor;
    }
    public void setInstructor(String instructors) {
        this.instructor = instructors;
    }
    public String getInstructorEmail() {
        return instructorEmail;
    }
    public void setInstructorEmail(String instructorsEmail) {
        this.instructorEmail = instructorsEmail;}
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type; }
    public String getLatitude() {
        return latitude;
    }
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
    public String getLongitude() {
        return longitude;
    }
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

}
