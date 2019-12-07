/******************************************************************************
Contributors:
Matthew Miceli
Christopher Chee
Shaun McFadden
Kareem Abdo 
*******************************************************************************/

// Apache Http Library imports
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

// Jsoup Library imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;

import java.util.List;
import java.util.ArrayList;

import java.io.PrintWriter;
import java.io.File;
import java.util.Scanner;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 *
 * @author CChee1
 */
public class CourseScraper {
    // This method loads text from the file argument passed into an array and returns it
    public static ArrayList<String> loadList (File file)
    {
        ArrayList<String> list = new ArrayList<String>();
        
        try
        {
            Scanner sc = new Scanner(file);

            while (sc.hasNextLine())
            {
                list.add(sc.nextLine());
            }
        }
        catch (FileNotFoundException f)
        {
            System.out.println("Error: File not found.");
        }
        
        sc.close();
        return list;
    }
    
    // Parses course information for department and returns array of course information
    public static ArrayList<String> parseTable(String pre)
    {
        ArrayList<String> lines = new ArrayList<String>();
        String line = "";

        for (int i = 0; i < pre.length(); i++)
        {
            char c = pre.charAt(i);

            if (c == '\n')
            {
                if (line.length() > 0)
                {
                    int charIndex = 0;
                    char firstChar = line.charAt(charIndex);
                    String firstWord = "";
                    
                    while (firstChar == ' ')
                    {
                        charIndex++;
                        firstChar = line.charAt(charIndex);
                    }

                    while (firstChar != ' ')
                    {
                        firstWord += firstChar;
                        charIndex++;

                        if (charIndex >= line.length())
                        {
                            break;
                        }

                        firstChar = line.charAt(charIndex);
                    }

                    if (line.charAt(0) == '(' || Character.isDigit(line.charAt(2)) || firstWord.compareTo("LAB") == 0)
                    {
                        lines.add(line);
                    }
                }
                
                line = "";
            }
            else
            {
                line += c;
            }
        }

        return lines;
    }
    
    /* This method connects to the LSU course scheduling website, passes in arguments
        from the semesters and departments lists, and parses the documents returned.
        The sanitized course information is then stored in text files. Because this method
        requests course information for each possible combination of semester, year, and 
        department, retrieving and parsing all the course information takes several hours. */
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        File coursemaindir = new File("CourseLists");

        if (!coursemaindir.exists())
        {
            coursemaindir.mkdirs();
        }

        // Reads Semesters.txt and Departments.txt and loads text into arrays
        File semlist = new File("SemDepLists\\Semesters.txt");
        File deptlist = new File("SemDepLists\\Departments.txt");
        ArrayList<String> semesters = loadList(semlist);
        ArrayList<String> departments = loadList(deptlist);
        ArrayList<String> errors = new ArrayList<String>();
        
        // Establishes connection to LSU course scheduling website
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://appl101.lsu.edu/booklet2.nsf/f5e6e50d1d1d05c4862584410071cd2e?CreateDocument");

        // Parameters to pass to http post request
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("%%Surrogate_SemesterDesc", "1"));
        params.add(new BasicNameValuePair("SemesterDesc", ""));
        params.add(new BasicNameValuePair("%%Surrogate_Department", "1"));
        params.add(new BasicNameValuePair("Department", ""));

        // Parses department course information for every semester and year
        for (String semester : semesters)
        {
            System.out.println(semester);

            File semdir = new File(coursemaindir.getPath() + "\\" + semester);
            
            if (!semdir.exists())
            {
                semdir.mkdirs();
            }
            
            params.set(1, new BasicNameValuePair("SemesterDesc", semester));

            // Parses course information for every department
            for (String department : departments)
            {
                System.out.println(department);

                params.set(3, new BasicNameValuePair("Department", department));
                post.setEntity(new UrlEncodedFormEntity(params));

                HttpResponse response = client.execute(post);
        
                String coursesURL = response.getLastHeader("Location").getValue();

                if (coursesURL.compareTo("https://appl101.lsu.edu/booklet2.nsf/NoCourseDept?readform") == 0)
                {
                    continue;
                }
        
                try
                {
                    Document doc = Jsoup.connect(coursesURL).maxBodySize(0).get();
                }
                catch (IOException goddammitJsoup)
                {
                    String exception = semester + " " + department;
                    errors.add(exception);

                    continue;
                }
                
                // Parses course information and writes it to a text file
                Document doc = Jsoup.connect(coursesURL).maxBodySize(0).get();
                PrintWriter writer = new PrintWriter(semdir.getPath() + "\\" + department + ".txt", "UTF-8");

                Elements pretags = doc.select("pre");
                Element pretag = pretags.get(0);
                String pretext = pretag.text();

                ArrayList<String> parsedCourses = parseTable(pretext);

                for (String line : parsedCourses)
                {
                    writer.printf("%s\n", line);
                }

                writer.close();
            }
        }

        // Error logging to track any potential problems
        File errlogdir = new File("Logs");

        if (!errlogdir.exists())
        {
            errlogdir.mkdirs();
        }

        PrintWriter errwriter = new PrintWriter(errlogdir.getPath() + "\\ErrorLog.txt");

        for (String error : errors)
        {
            errwriter.println(error);
        }

        errwriter.close();
    }  
}