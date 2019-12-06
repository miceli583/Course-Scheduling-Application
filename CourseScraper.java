import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;

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
        
        return list;
    }
    
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
    
    public static void main(String[] args) throws IOException, FileNotFoundException {
        
        File coursemaindir = new File("CourseLists");

        if (!coursemaindir.exists())
        {
            coursemaindir.mkdirs();
        }

        File semlist = new File("SemDepLists\\Semesters.txt");
        File deptlist = new File("SemDepLists\\Departments.txt");
        ArrayList<String> semesters = loadList(semlist);
        ArrayList<String> departments = loadList(deptlist);
        ArrayList<String> errors = new ArrayList<String>();
        
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://appl101.lsu.edu/booklet2.nsf/f5e6e50d1d1d05c4862584410071cd2e?CreateDocument");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("%%Surrogate_SemesterDesc", "1"));
        params.add(new BasicNameValuePair("SemesterDesc", ""));
        params.add(new BasicNameValuePair("%%Surrogate_Department", "1"));
        params.add(new BasicNameValuePair("Department", ""));

        for (String semester : semesters)
        {
            /*if (semester.compareTo("Spring 2020") != 0)
            {
                continue;
            }*/

            System.out.println(semester);

            File semdir = new File(coursemaindir.getPath() + "\\" + semester);
            
            if (!semdir.exists())
            {
                semdir.mkdirs();
            }
            
            params.set(1, new BasicNameValuePair("SemesterDesc", semester));

            for (String department : departments)
            {
                System.out.println(department);

                /*if (department.compareTo("CURRICULUM & INSTRUCTION") == 0)
                {
                    continue;
                }*/

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