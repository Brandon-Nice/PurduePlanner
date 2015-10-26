import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
/*import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;*/



public class Classes {
	
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
	
	/*// Adds a class to the database
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
	       
	   
	}*/
	
	// Write an arraylist of classes to a file
	public static void writeClassesToFile(ArrayList<Classes> classes, String filename)
	{
		try 
		{
        	// Code to write classes to text file
			PrintWriter out = new PrintWriter(filename);
			for (int i = 0; i < classes.size(); i++)
			{
				Classes currentClass = classes.get(i);
				out.print("\"" + currentClass.getStartTime() + "\"");
				out.print("\"" + currentClass.getEndTime() + "\"");
				out.print("\"" + currentClass.getStartDate() + "\"");
				out.print("\"" + currentClass.getEndDate() + "\"");
				out.print("\"" + currentClass.getCRN() + "\"");
				out.print("\"" + currentClass.getMajor() + "\"");
				out.print("\"" + currentClass.getCourseNum() + "\"");
				out.print("\"" + currentClass.getSectionNum() + "\"");
				out.print("\"" + currentClass.getCredits() + "\"");
				out.print("\"" + currentClass.getTitle() + "\"");
				out.print("\"" + currentClass.getDays() + "\"");
				out.print("\"" + currentClass.getInstructor() + "\"");
				out.print("\"" + currentClass.getInstructorEmail() + "\"");
				out.print("\"" + currentClass.getLocation() + "\"");
				out.println("\"" + currentClass.getType() + "\"");
			}
			out.close();
		} 
		catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Read an arraylist of classes from a file
	public static ArrayList<Classes> readClassesFromFile(String filename)
	{
		// Code to read classes from text file
     	ArrayList<Classes> readClasses = new ArrayList<Classes>();
     	try 
     	{
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line = null;
			while((line = bufferedReader.readLine()) != null) 
			{
				Classes currentClass = new Classes();
				//System.out.println(line);
                int startTimeStart = line.indexOf("\"") + 1;
                int startTimeEnd = line.indexOf("\"", startTimeStart);
                currentClass.setStartTime(line.substring(startTimeStart, startTimeEnd));
                
                int endTimeStart = line.indexOf("\"", startTimeEnd + 1) + 1;
                int endTimeEnd = line.indexOf("\"", endTimeStart);
                currentClass.setEndTime(line.substring(endTimeStart, endTimeEnd));
                
                int startDateStart = line.indexOf("\"", endTimeEnd + 1) + 1;
                int startDateEnd = line.indexOf("\"", startDateStart);
                currentClass.setStartDate(line.substring(startDateStart, startDateEnd));
                
                int endDateStart = line.indexOf("\"", startDateEnd + 1) + 1;
                int endDateEnd = line.indexOf("\"", endDateStart);
                currentClass.setEndDate(line.substring(endDateStart, endDateEnd));
                
                int crnStart = line.indexOf("\"", endDateEnd + 1) + 1;
                int crnEnd = line.indexOf("\"", crnStart);
                currentClass.setCRN(line.substring(crnStart, crnEnd));
                
                int majorStart = line.indexOf("\"", crnEnd + 1) + 1;
                int majorEnd = line.indexOf("\"", majorStart);
                currentClass.setMajor(line.substring(majorStart, majorEnd));
                
                int coursenumStart = line.indexOf("\"", majorEnd + 1) + 1;
                int coursenumEnd = line.indexOf("\"", coursenumStart);
                currentClass.setCourseNum(line.substring(coursenumStart, coursenumEnd));
                
                int sectionnumStart = line.indexOf("\"", coursenumEnd + 1) + 1;
                int sectionnumEnd = line.indexOf("\"", sectionnumStart);
                currentClass.setSectionNum(line.substring(sectionnumStart, sectionnumEnd));
                
                int creditsStart = line.indexOf("\"", sectionnumEnd + 1) + 1;
                int creditsEnd = line.indexOf("\"", creditsStart);
                currentClass.setCredits(line.substring(creditsStart, creditsEnd));
                
                int titleStart = line.indexOf("\"", creditsEnd + 1) + 1;
                int titleEnd = line.indexOf("\"", titleStart);
                currentClass.setTitle(line.substring(titleStart, titleEnd));
                
                int daysStart = line.indexOf("\"", titleEnd + 1) + 1;
                int daysEnd = line.indexOf("\"",  daysStart);
                currentClass.setDays(line.substring(daysStart, daysEnd));
                
                int instructorStart = line.indexOf("\"", daysEnd + 1) + 1;
                int instructorEnd = line.indexOf("\"", instructorStart);
                currentClass.setInstructor(line.substring(instructorStart, instructorEnd));
                
                int emailStart = line.indexOf("\"", instructorEnd + 1) + 1;
                int emailEnd = line.indexOf("\"", emailStart);
                currentClass.setInstructorEmail(line.substring(emailStart, emailEnd));
                
                int locationStart = line.indexOf("\"", emailEnd + 1) + 1;
                int locationEnd = line.indexOf("\"", locationStart);
                currentClass.setLocation(line.substring(locationStart, locationEnd));
                
                int typeStart = line.indexOf("\"", locationEnd + 1) + 1;
                int typeEnd = line.indexOf("\"", typeStart);
                currentClass.setType(line.substring(typeStart, typeEnd));
                
                readClasses.add(currentClass);
            }  
		    bufferedReader.close();         
        }
     	catch(FileNotFoundException ex) 
     	{
            System.out.println("Unable to open file '" + filename + "'");                
        }
        catch(IOException ex) 
     	{
            System.out.println("Error reading file '" + filename + "'");    
        }
     	
     	return readClasses;
	}
	
	
	
	// Convert classes to a string
	@Override 
	public String toString()
	{
		String startTime = "Start Time: " +  getStartTime() + "\n";
		String endTime = "End Time : " + getEndTime() + "\n";
		String startDate = "Start Date: " +  getStartDate() + "\n";
		String endDate = "End Date : " + getEndDate() + "\n";
		String crn = "CRN: " + getCRN() + "\n";
		String major = "Major: " + getMajor() + "\n";
		String courseNum = "Course Number: " + getCourseNum() + "\n";
		String sectionNum = "Section NUmber: " + getSectionNum() + "\n";
		String credits = "Credits: " + getCredits() + "\n";
		String title = "Title: " + getTitle() + "\n";
		String days = "Days: " + getDays() + "\n";
		String instructor = "Primary instructor: " + getInstructor() + "\n";
		String instructorEmail = "Primary instructor email: " + getInstructorEmail() + "\n";
		String location = "Location: " + getLocation() + "\n";
		String type = "Type: " + getType();
		
		String total = startTime + endTime + startDate + endDate + crn + major +
				courseNum + sectionNum + credits + title + days + instructor +
				instructorEmail + location + type;
		return total;
		
	}
}
