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

public class UpdateSem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        String semester = args[0];
        
        for (int i = 1; i < args.length; i++)
        {
            semester += " " + args[i];
        }

        System.out.println(semester);

        File dirpath = new File("CourseLists\\" + semester);

        if (!dirpath.exists())
        {
            dirpath.mkdirs();
        }

        ArrayList<String> departments = new ArrayList<String>();
        ArrayList<String> errors = new ArrayList<String>();

        String url = "http://appl101.lsu.edu/booklet2.nsf/mainframeset";
        Document main = Jsoup.connect(url).get();
        Element frame = main.select("frame").get(0);
        Document selectFrame = Jsoup.connect(frame.absUrl("src")).get();
        Elements options = selectFrame.getElementsByAttributeValue("name", "Department").get(0).children();

        for (Element option : options)
        {
            departments.add(option.text());
        }

        url = "http://appl101.lsu.edu/booklet2.nsf/f5e6e50d1d1d05c4862584410071cd2e?CreateDocument";
        
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://appl101.lsu.edu/booklet2.nsf/f5e6e50d1d1d05c4862584410071cd2e?CreateDocument");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("%%Surrogate_SemesterDesc", "1"));
        params.add(new BasicNameValuePair("SemesterDesc", semester));
        params.add(new BasicNameValuePair("%%Surrogate_Department", "1"));
        params.add(new BasicNameValuePair("Department", ""));

        for (String department :departments)
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
                Document doc = Jsoup.connect(coursesURL).get();
            }
            catch (IOException goddammitJsoup)
            {
                String exception = semester + " " + department;
                errors.add(exception);
                continue;
            }

            Document doc = Jsoup.connect(coursesURL).maxBodySize(0).get();
            Elements pretags = doc.select("pre");
            Element pretext = pretags.get(0);

            ArrayList<String> lines = new ArrayList<String>();
            String pre = pretext.text();
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

            PrintWriter writer = new PrintWriter(dirpath + "\\" + department + ".txt", "UTF-8");

            for (String courseInfo : lines)
            {
                writer.printf("%s\n", courseInfo);
            }

            writer.close();
        }

        /*File errlogdir = new File("Logs");

        if (!errlogdir.exists())
        {
            errlogdir.mkdirs();
        }

        PrintWriter errwriter = new PrintWriter(errlogdir + "\\ErrorLog.txt");

        for (String error : errors)
        {
            errwriter.println(error);
        }

        errwriter.close();*/
    }
}