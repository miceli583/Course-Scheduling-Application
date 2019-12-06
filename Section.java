/******************************************************************************
Contributors:
Matthew Miceli
Christopher Chee
Shaun McFadden
Kareem Abdo 
*******************************************************************************/
// Section Class - defines attributes of a Section
public class Section
{
        String enrollmentCount;
        String avail;
        String sectionNum;
        double[] startTime;
        double[] endTime;
        double[] labStartTime;
        double[] labEndTime;
        String roomBuild;
        String teacher;
        
        public Section(String e, String a, String n, double[] st, double[] et, double[] lst, double[] let, String r, String t){
            enrollmentCount = e;
            avail = a;
            sectionNum = n;
            startTime = st;
            endTime = et;
            labStartTime = lst;
            labEndTime = let;
            roomBuild = r;
            teacher = t;
        }
}
