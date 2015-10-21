import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;


public class Classes implements Serializable{
	
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
	
	// Default constructor
	public Classes()
	{
		
	}
	// Constructor that can set all the variables needed
	public Classes(String startTime, String endTime, String startDate, String endDate, 
			String CRN, String major, String courseNum, String section, double credits, 
			String title, String days, String instructors, String location, String type)
	{
		
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
		this.type = type;
	}
	public String getInstructorEmail() {
		return instructorEmail;
	}
	public void setInstructorEmail(String instructorsEmail) {
		this.instructorEmail = instructorsEmail;
	}
	
	// Adds a class to the database
	public void addClassToSQLDataBase()
	{
		// This is the database that will stores the class
		String url = "jdbc:mysql://localhost:3306/PurduePlanner";
		// Username for sql database
		String username = "root";
		// Password for sql database
		String password = "root";

		System.out.println("Connecting database...");
		
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			// Will print when connection to database is achieved
			System.out.println("Database connected!");
			// the mysql insert statement
			String query = " insert into classes (startTime, endTime, startDate, "
					+ "endDate, CRN, major, courseNum, section, credits, title, days, "
					+ "instructors, instructorEmail, location, type)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = connection.prepareStatement(query);
			
			// These prepared statements fill in each of the ? in the insert statement
			if (startTime.equals("TBA"))
			{
				preparedStmt.setNull(1, Types.VARCHAR);
			}
			else
			{
				preparedStmt.setString(1, startTime);
			}
			if (endTime.equals("TBA"))
			{
				preparedStmt.setNull(2, Types.VARCHAR);
			}
			else
			{
				preparedStmt.setString(2, endTime);
			}
			preparedStmt.setString(3, startDate);
			preparedStmt.setString(4, endDate);
			preparedStmt.setString(5, CRN);
			preparedStmt.setString(6, major);
			preparedStmt.setString(7, courseNum);
			preparedStmt.setString(8, sectionNum);
			preparedStmt.setString(9, credits);
			preparedStmt.setString(10, title);
			if (days.equals("TBA"))
			{
				preparedStmt.setNull(11, Types.VARCHAR);
			}
			else
			{
				preparedStmt.setString(11, days);
			}
			if (instructor.equals("TBA"))
			{
				preparedStmt.setNull(12, Types.VARCHAR);
			}
			else
			{
				preparedStmt.setString(12, instructor);
			}
			if (instructorEmail.equals("TBA"))
			{
				preparedStmt.setNull(13, Types.VARCHAR);
			}
			else
			{
				preparedStmt.setString(13, instructorEmail);
			}
			if (location.equals("TBA"))
			{
				preparedStmt.setNull(14, Types.VARCHAR);
			}
			else
			{
				preparedStmt.setString(14, location);
			}
			preparedStmt.setString(15, type);
			// execute the prepared statement
			preparedStmt.execute();
			// close the connection
			connection.close();
		} catch (SQLException e) {
			// Will print if connection to the database cannot be made
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	       
	   
	}
}
