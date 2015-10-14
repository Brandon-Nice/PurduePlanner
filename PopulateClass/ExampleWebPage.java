
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.ui.Select;


public class ExampleWebPage {
	public static void main(String[] args)  {
        HtmlUnitDriver driver = new HtmlUnitDriver();
        driver.get("https://selfservice.mypurdue.purdue.edu/prod/bwckschd.p_disp_dyn_sched");
        Select semester = new Select(driver.findElement(By.name("p_term")));
        List<WebElement> semesterOptions = semester.getOptions();
        semesterOptions.get(2).click();
        semesterOptions.get(2).submit();
        
        Select majors = new Select(driver.findElement(By.id("subj_id")));
        List<WebElement> majorsOptions = majors.getOptions();
		for (int i = 0; i < 1; i++)
		{
			driver.get("https://selfservice.mypurdue.purdue.edu/prod/bwckschd.p_disp_dyn_sched");
			semester = new Select(driver.findElement(By.name("p_term")));
	        semesterOptions = semester.getOptions();
			semesterOptions.get(2).click();
	        semesterOptions.get(2).submit();
	        
	        majors = new Select(driver.findElement(By.id("subj_id")));
	        majorsOptions = majors.getOptions();
	        majorsOptions.get(i).click();
	        majorsOptions.get(i).submit();
	        
	        Classes currentClass;
	        int firstOccurenceofDDLABEL = driver.getPageSource().indexOf("ddlabel");
	        int secondOccurenceofDDLABEL = driver.getPageSource().indexOf("ddlabel", firstOccurenceofDDLABEL + 1);
	        while (secondOccurenceofDDLABEL != -1)
	        {
	        	currentClass = new Classes();
		        String sub = driver.getPageSource().substring(firstOccurenceofDDLABEL + 1, secondOccurenceofDDLABEL);
		        //System.out.println(sub); 
		        int startTitle = sub.indexOf(">", sub.indexOf(">") + 1);
		        int endTitle = sub.indexOf("<", startTitle + 1);
		        // This string contains the title, crn, major, course number, and section id
		        String tCMCS = sub.substring(startTitle + 1, endTitle).trim();
		        int firstTCMCSDash = tCMCS.indexOf(" - ");
		        int secondTCMCSDash = tCMCS.indexOf(" - ", firstTCMCSDash + 1);
		        int thirdTCMCSDash = tCMCS.indexOf(" - ", secondTCMCSDash + 1);
		        currentClass.setTitle(tCMCS.substring(0, firstTCMCSDash));
		        currentClass.setCRN(tCMCS.substring(firstTCMCSDash + 3, secondTCMCSDash));
		        String majorSection = tCMCS.substring(secondTCMCSDash + 3, thirdTCMCSDash);
		        currentClass.setMajor(majorSection.substring(0, majorSection.indexOf(" ")));
		        currentClass.setCourseNum(majorSection.substring(majorSection.indexOf(" ") + 1));
		        currentClass.setSectionNum(tCMCS.substring(thirdTCMCSDash + 3));
		        //System.out.println("Title: \"" + title + "\"");
		        //System.out.println("CRN: \"" + crn + "\"");
		        //System.out.println("Major: \"" + major + "\"");
		        //System.out.println("Course Number: \"" + courseNum + "\"");
		        //System.out.println("Section Number: \"" + sectionNum + "\"");
		        int creditsPlace = sub.indexOf("Credits");
		        currentClass.setCredits(Double.parseDouble(sub.substring(creditsPlace - 6, creditsPlace - 1)));
		        //System.out.println("Credits: \"" + credits + "\"");
		        
		        int firstDDDEFAULT = sub.indexOf("dddefault\">", creditsPlace);
		        int secondDDDEFAULT = sub.indexOf("dddefault\">", firstDDDEFAULT + 1);
		        int thirdDDDEFAULT = sub.indexOf("dddefault\">", secondDDDEFAULT + 1);
		        String startTime;
		        String endTime;
		        if (sub.substring(secondDDDEFAULT + 1, thirdDDDEFAULT).contains("TBA"))
		        {
		        	startTime = "TBA";
		        	endTime = "TBA";
		        }
		        else
		        {
			        int startOfTimes = sub.indexOf(">", secondDDDEFAULT);
			        int endOfTimes = sub.indexOf("<", startOfTimes + 1);
			        String totalTimes = sub.substring(startOfTimes + 1, endOfTimes).trim();
			        startTime = totalTimes.substring(0, totalTimes.indexOf("-") - 1);
			        endTime = totalTimes.substring(totalTimes.indexOf("-") + 2);
		        }
		        currentClass.setStartTime(startTime);
		        currentClass.setEndTime(endTime);
		        //System.out.println("Start Time: \"" + startTime + "\"");
		        //System.out.println("End Time: \"" + endTime + "\"");
		        
		        int startOfDays = sub.indexOf(">", thirdDDDEFAULT);
		        int endOfDays = sub.indexOf("<", startOfDays + 1);
		        String totalDays = sub.substring(startOfDays + 1, endOfDays).trim();
		        if (!totalDays.contains("M") && !totalDays.contains("T") 
		        		&& !totalDays.contains("W") && ! totalDays.contains("R")
		        		&& !totalDays.contains("F"))
		        {
		        	totalDays = "TBA";
		        }
		        currentClass.setDays(totalDays);
		        //System.out.println("Days: \"" + totalDays + "\"");
		        
		        int fourthDDDEFAULT = sub.indexOf("dddefault\">", thirdDDDEFAULT + 1);
		        int fifthDDDEFAULT = sub.indexOf("dddefault\">", fourthDDDEFAULT + 1);
		        String totalLocation;
		        if (sub.substring(fourthDDDEFAULT + 1, fifthDDDEFAULT).contains("TBA"))
		        {
		        	totalLocation = "TBA";
		        }
		        else
		        {
			        int startOfLocation = sub.indexOf(">", fourthDDDEFAULT);
			        int endOfLocation = sub.indexOf("<", startOfLocation + 1);
			        totalLocation = sub.substring(startOfLocation + 1, endOfLocation).trim();
		        }
		        currentClass.setLocation(totalLocation);
			    //System.out.println("Location: \"" + totalLocation + "\"");
		        
		        int startOfDate = sub.indexOf(">", fifthDDDEFAULT);
		        int endOfDate = sub.indexOf("<", startOfDate + 1);
		        String totalDate = sub.substring(startOfDate + 1, endOfDate).trim();
		        currentClass.setStartDate(totalDate.substring(0, totalDate.indexOf("-") - 1));
		        currentClass.setEndDate(totalDate.substring(totalDate.indexOf("-") + 2));
		        //System.out.println("Start Date: \"" + startDate + "\"");
		        //System.out.println("End Date: \"" + endDate + "\"");
		        
		        int sixthDDDEFAULT = sub.indexOf("dddefault\">", fifthDDDEFAULT + 1);
		        int startOfType = sub.indexOf(">", sixthDDDEFAULT);
		        int endOfType = sub.indexOf("<", startOfType + 1);
		        currentClass.setType(sub.substring(startOfType + 1, endOfType).trim());
		        //System.out.println("Type: \"" + totalType + "\"");
		        
		        int seventhDDDEFAULT = sub.indexOf("dddefault\">", sixthDDDEFAULT + 1);
		        String totalInstructor;
		        String totalEmail;
		        if (sub.substring(seventhDDDEFAULT).contains("TBA"))
		        {
		        	totalInstructor = "TBA";
		        	totalEmail = "TBA";
		        }
		        else
		        {
			        int startOfInstructor = sub.indexOf(">", seventhDDDEFAULT);
			        int endOfInstructor = sub.indexOf("<", startOfInstructor + 1);
			        totalInstructor = sub.substring(startOfInstructor + 1, endOfInstructor).trim();
			        totalInstructor = totalInstructor.substring(0, totalInstructor.length() - 2);
			        totalInstructor = totalInstructor.replaceAll("\\s+", " ");
			        //System.out.println("Instructor: \"" + totalInstructor + "\"");
			        int emailPlace = sub.indexOf("mailto", seventhDDDEFAULT);
			        int startOfEmail = sub.indexOf(":", emailPlace);
			        int endOfEmail = sub.indexOf("\"", startOfEmail + 1);
			        totalEmail = sub.substring(startOfEmail + 1, endOfEmail);
			        //System.out.println("Instructor Email: \"" + totalEmail + "\"");
		        }
		        currentClass.setInstructor(totalInstructor);
		        currentClass.setInstructorEmail(totalEmail);
		        
		        currentClass.addClassToSQLDataBase();
		        
		        firstOccurenceofDDLABEL = secondOccurenceofDDLABEL;
		        secondOccurenceofDDLABEL = driver.getPageSource().indexOf("ddlabel", firstOccurenceofDDLABEL + 1); 
	        }
		}
        
        driver.quit();
    }


}
