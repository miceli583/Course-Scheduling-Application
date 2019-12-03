/******************************************************************************

Welcome to GDB Online.
GDB online is an online compiler and debugger tool for C, C++, Python, Java, PHP, Ruby, Perl,
C#, VB, Swift, Pascal, Fortran, Haskell, Objective-C, Assembly, HTML, CSS, JS, SQLite, Prolog.
Code, Compile, Run and Debug online from anywhere in world.

*******************************************************************************/
import java.io.FileInputStream;
import java.util.Scanner;
import java.util.ArrayList;

public class Main
{
public static Boolean compare(double st1, double et1, double st2, double et2){
        if (st1 > st2 && st1 < et2) {return true;}
        if (et1 > st2 && et1 < et2) {return true;}
        return false;
}

public static Boolean isOverlap(Section S1, Section S2){
    double[] st1 = S1.startTime;
    double[] et1 = S1.endTime;
    double[] st2 = S2.startTime;
    double[] et2 = S2.endTime;
    for(int i = 0; i<5; i++){
        if(compare(st1[i], et1[i], st2[i], et2[i])){return true;}
    }
    return false;
}

public static void courseAdder(Course c, ArrayList<ArrayList<Section>> sectionMaster){
    Section temp;
    ArrayList<Section> list = new ArrayList<Section>(1);
    list.add(null);
    Boolean t = false;
    if(sectionMaster.isEmpty()){
            for(int i = 0; i< c.numSections; i++){
                temp = c.sections.get(i);
                list.set(0,temp);
                sectionMaster.add(list);
            }
        }
    else{
        ArrayList<ArrayList<Section>> tempMaster = sectionMaster;
        int z = tempMaster.size();
        for(int k = 0; k<c.numSections; k++){
            //ArrayList<ArrayList<Section>> adder = tempMaster;
            temp = c.sections.get(k);
            for(int j = 0; j<z; j++){
                if(k==0){
                    list = tempMaster.get(j);
                    for(int a = 0; a<list.size(); a++){
                        if(isOverlap(temp, list.get(a))){t = true;}
                    }
                    if(t){list = null;} 
                    else{list.add(temp);}
                    sectionMaster.set(j,list);
                }
                else{
                    list = tempMaster.get(j);
                    for(int a = 0; a<list.size(); a++){
                        if(isOverlap(temp, list.get(a))){t = true;}
                    }
                    if(t){list = null;} 
                    else{list.add(temp);}
                    sectionMaster.add(list);
                }
            }
        }
    }
}
public static void main (String[]args)
{
    
    
    Course testCourse;
    
    String deptAbbrev = "";
    String courseNum = "";
    int numSections = 0;
    String courseTitle = "";
    String enrollmentCount = "";
    String avail = "";
    String sectionNum = "";
    double[] startTime = new double []{0,0,0,0,0};
    double[] endTime = new double []{0,0,0,0,0};
    double[] labStartTime = new double []{0,0,0,0,0};
    double[] labEndTime = new double []{0,0,0,0,0};
    String roomBuild = "";
    String teacher = "";
    String temp;
    double st=0;
    double et=0;
    
    int q;
    char ch;
    
    Scanner input = new Scanner (System.in);
    System.out.println ("Enter Semester (Month Year)");
	String mon = input.next();
	String year = input.next();
	String SEMESTER = mon + " " + year;
	
    ArrayList<Course> courses = new ArrayList<Course>();
    ArrayList<Character> reader =  new ArrayList<Character>();
    int status = 1;
    int k = 0;
    while (status == 1)
    {
	    
	    System.out.println ("Enter Dept Name");
	    String DEPARTMENT = input.next();
	    System.out.println ("Enter Dept Abbreviation");
	    String deptABBREVIATION = input.next();
	    System.out.println ("Enter Course Number(####)");
	    String COURSENUM = input.next();
        
        Course tempCourse = new Course(SEMESTER, DEPARTMENT, deptABBREVIATION, COURSENUM);
        courses.add(tempCourse);

	    try{
	        FileInputStream fin = new FileInputStream ("Accounting.txt");
	        int i = 0;
	        q = 0;
	        
	        while ((i = fin.read ()) != -1){
	            ch = (char)i;
	            reader.add(q,ch);
	            
	            if(ch == '\n'){
	                
	                for(int j = 0; j<=2; j++){enrollmentCount += String.valueOf(reader.get(j));}
	                for(int j = 6; j<=8; j++){avail += String.valueOf(reader.get(j));}
	                for(int j = 11; j<=14; j++){deptAbbrev += String.valueOf(reader.get(j));} deptAbbrev=deptAbbrev.trim();
	                for(int j = 16; j<=19; j++){courseNum += String.valueOf(reader.get(j));}
	                for(int j = 27; j<=29; j++){sectionNum += String.valueOf(reader.get(j));}
	                for(int j = 32; j<=51; j++){courseTitle += String.valueOf(reader.get(j));}
	                if(reader.get(60) != ' '){st += 10*(Character.digit(reader.get(60),10));}
	                et += 10*(Character.digit(reader.get(65),10));
	                st += (Character.digit(reader.get(61),10));
	                et += (Character.digit(reader.get(66),10));
	                if(reader.get(62) == '3'){st += 0.5;}
	                if(reader.get(67) == '2'){et += 0.33;}
	                if(reader.get(67) == '5'){et += 0.83;}
	                if (reader.get(69) == 'N' || st <7){st += 12;}
	                if(et<8){et += 12;}
	                if(reader.get(60) == 'T'){st = 0;et = 0;}
	                if(reader.get(72) == 'M'){startTime[1] = st; endTime[1] = et;}
	                if(reader.get(73) == 'T'){startTime[2] = st; endTime[2] = et;}
	                if(reader.get(74) == 'W'){startTime[3] = st; endTime[3] = et;}
	                if(reader.get(75) == 'T'){startTime[4] = st; endTime[4] = et;}
	                if(reader.get(77) == 'F'){startTime[5] = st; endTime[5] = et;}
	                for(int j = 80; j<=99; j++){roomBuild += String.valueOf(reader.get(j));}
	                for(int j = 117; j<q; j++){teacher += String.valueOf(reader.get(j));}
	                
	                
	                //Construct Course and Sections
	                if (deptABBREVIATION.equals(deptAbbrev) && COURSENUM.equals(courseNum)){
	                    Section tempSection = new Section(enrollmentCount, avail, sectionNum, startTime, endTime, labStartTime, labEndTime, roomBuild, teacher);
	                    courses.get(k).sections.add(tempSection);
	                    courses.get(k).update();
	                }
	                
	                deptAbbrev = "";
                    courseNum = "";
                    numSections = 0;
                    courseTitle = "";
                    enrollmentCount = "";
                    avail = "";
                    sectionNum = "";
                    startTime = new double []{0,0,0,0,0};
                    endTime = new double []{0,0,0,0,0};
                    labStartTime = new double []{0,0,0,0,0};
                    labEndTime = new double []{0,0,0,0,0};
                    roomBuild = "";
                    teacher = "";
                    temp = "";
                    st=0;
                    et=0;
	                reader.clear();
	                q = -1;
	                
	            
	            }
	            
	            q +=1;
            }
	        fin.close ();
	    } catch (Exception e){System.out.println (e);}
	    
	    k +=1;
	    System.out.println ("If done, enter 0. Otherwise, enter 1");
	    status = input.nextInt();
    }
    
    
    
    ArrayList<ArrayList<Section>> sectionMaster = new ArrayList<ArrayList<Section>>();
    for(int b = 0; b<courses.size(); b++){
        courseAdder(courses.get(b), sectionMaster);
    }
    


}

}
