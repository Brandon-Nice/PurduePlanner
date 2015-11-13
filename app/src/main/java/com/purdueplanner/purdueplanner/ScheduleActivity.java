package com.purdueplanner.purdueplanner;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.app.Activity;
import android.view.Menu;
import android.widget.ExpandableListView;


/**
 * Created by menane on 10/13/15. Reference: http://www.survivingwithandroid.com/2013/01/android-expandablelistview-baseexpandablelistadapter.html
 */

public class ScheduleActivity extends FragmentActivity {
    private List<Category> catList;

    private String[] testArray = {"CS 354", "CS 252", "CS 348", "CS 391", "CS 307", "CS 381", "CS 250"};
    private ExpandableListView weekListView;
    private ArrayList<Classes> studentsClasses = new ArrayList<Classes>(); //stores the students classes
    //private ArrayAdapter arrayAdapter;

    @Override
    //provide the onCreate method to apply the Schedule layout to the activity
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData(); //initializes the categories and the items
        setContentView(R.layout.activity_schedule);

        ExpandableListView exList = (ExpandableListView) findViewById(R.id.weekdayView);
        exList.setIndicatorBounds(7, 7); //sets the bounds to start and end for the category aka the week
        ExpandableAdapter exAdpt = new ExpandableAdapter(catList, this);
        exList.setIndicatorBounds(0, 20); //sets the bounds for the start and end for the items aka the classes TODO: Make dynamic for students classes
        exList.setAdapter(exAdpt); //sets the data from the category (with the item lists) to be displayed in the ExpandableListView

        studentsClasses = ((MyApplication)getApplication()).getStudent().getSchedule();
        //weekListView.setAdapter(studentclasses)
    }

    //Sets the days of the week as Categories
    private void initData() {
        catList = new ArrayList<Category>();
        int classesCount = studentsClasses.size(); //define this local var to be the parameter for when we create our lists
        //sets each day (cat) with the associated list of classes (result from createItems)
        Category cat1 = new Category(1,"","Monday");
        cat1.setItemList(createItems("Mondays classes", "", classesCount, "M"));

        Category cat2 = new Category(2, "", "Tuesday");
        cat2.setItemList(createItems("Tuesdays classes", "", classesCount, "T"));

        Category cat3 = new Category(3, "", "Wednesday");
        cat3.setItemList(createItems("Weds classes", "", classesCount, "W"));

        Category cat4 = new Category(4, "", "Thursday");
        cat4.setItemList(createItems("Thurs classes", "", classesCount, "R"));

        Category cat5 = new Category(5, "", "Friday");
        cat5.setItemList(createItems("Friday classes", "", classesCount, "F"));

        Category cat6 = new Category(6, "", "Saturday");
        cat6.setItemList(createItems("Saturday classes", "", classesCount, "S"));

        Category cat7 = new Category(7, "", "Sunday");
        cat7.setItemList(createItems("Sunday classes", "", classesCount, "Su"));

        //add it to the category list that is to be displayed
        catList.add(cat1);
        catList.add(cat2);
        catList.add(cat3);
        catList.add(cat4);
        catList.add(cat5);
        catList.add(cat6);
        catList.add(cat7);
    }
    //This creates a "List" item of classes per day for the student. Num indicates the number of items, which in our case is the number
    //of classes. Essentially, this makes an ArrayList for the classes per day.
    private List<ItemDetail> createItems(String name, String description, int num, String day) {
        List<ItemDetail> result = new ArrayList<ItemDetail>(); //creates the List result that is to be returned
        int counterId = 0; //makes each item have a unique ID
        for (int i=0; i < num; i++) {  //Iterate through the number of classes that the student currently has
            //while(classesCount != 0) {
            if(studentsClasses.get(i).getDays().contains(day)) { //Match the classes by the day that is passed in so it can be displayed by each day
                System.out.println("What is this " + studentsClasses.get(i).getTitle());
                ItemDetail classItem = new ItemDetail(counterId, 0, studentsClasses.get(i).getTitle(), studentsClasses.get(i).getLocation());
                result.add(classItem);
                counterId++;
            }
            //    result
            //    counter++;
            //}
            //i = counter;
            //ItemDetail item = new ItemDetail(i, 0, "item" + i, "Descr" + i);
        }

        return result;
    }
}





