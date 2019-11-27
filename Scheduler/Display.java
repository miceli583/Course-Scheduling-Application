import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Display
{
	public static void display(ArrayList<ArrayList<Section>> schedules) 
	{
	    String[][] table = new String[48][6];
	    for(int a = 0; a < 48; a++)
	    {
	        String time = "";
	        double b = a/2.0;
	        if(b >= 13)
	        {
	            b = b - 12;
	            time += b;
	            if (b != a/2)
	                time += ":30 PM";
	            else
	                time += ":00 PM";
	        }
	        else
	        {
	            time += b;
	            if (b != a/2)
	                time += ":30 AM";
	            else
	                time += ":00 AM";
	        }
	        table[a][0] = time;
	    }
	    String[][] reset = table.clone();
	    
	    for(int i = 0; i < schedules.size(); i++)
	    {
    	    for(int j = 0; i < schedules.get(i).size(); j++)
    		{
    		    for(int c = 1; c < 6; c++)
    		    {
    		        Section section = schedules.get(i).get(j);
    		        if(section.startTime[c] != 0)
    		        {
        		        table[section.startTime[c]/.5][c] = section.deptAbbrev
        		            + " " + section.courseNum + " starts";
        		        table[section.endTime[c]/.5][c] = section.deptAbbrev
        		            + " " + section.courseNum + " ends";
    		        }
    		    }
    		    System.out.printf("%-20s %-20s %-20s %-20s %-20s %-20s%n",
    		    "", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday");
    		    for(int t = 12; t < 41; t++)
    		    {
    		        for(int d = 0; d < 6; d++)
    		        {
    		            System.out.printf("%20s", table[t][d]);
    		        }
    		        System.out.println("");
    		    }
    		}
	    }
	}
}
