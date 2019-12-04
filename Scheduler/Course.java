import java.util.ArrayList;

public class Course
{
        String semester;
        String dept;
        String deptAbbrev;
        String courseNum;
        int numSections = 0;
        String courseTitle;

        ArrayList<Section> sections = new ArrayList<Section>();

        void update(){
                numSections = sections.size();
        }

        public Course(String s, String d, String a, String n){
                semester = s;
                dept = d;
                deptAbbrev = a;
                courseNum = n;
        }
        
        public Course(String s, String d, String n){
                semester = s;
                dept = d;
                courseNum = n;
        }
}
