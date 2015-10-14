import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;


public class Classes {
	
	private String startTime;
	private String endTime;
	private String startDate;
	private String endDate;
	private String CRN;
	private String major;
	private String courseNum;
	private String sectionNum;
	private double credits;
	private String title;
	private String days;
	private String instructor;
	private String instructorEmail;
	private String location;
	private String type;
	
	
	public Classes()
	{
		
	}
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
	public double getCredits() {
		return credits;
	}
	public void setCredits(double credits) {
		this.credits = credits;
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
	
	//Must be connected to database for this to work
	public void addClassToSQLDataBase()
	{
		String url = "jdbc:mysql://localhost:3306/PurduePlanner";
		String username = "root";
		String password = "root";

		System.out.println("Connecting database...");
		
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			System.out.println("Database connected!");
			// the mysql insert statement
			String query = " insert into classes (startTime, endTime, startDate, "
					+ "endDate, CRN, major, courseNum, section, credits, title, days, "
					+ "instructors, instructorEmail, location, type)"
					+ " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = connection.prepareStatement(query);
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
			preparedStmt.setDouble(9, credits);
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
			// execute the preparedstatement
			preparedStmt.execute();
			connection.close();
		} catch (SQLException e) {
		    throw new IllegalStateException("Cannot connect the database!", e);
		}
	       
	   
	}
}
