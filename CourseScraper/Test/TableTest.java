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

public class TableTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        File dirpath = new File("C:\\Users\\CChee1\\Desktop\\CSC 3380\\TigerPlanner\\Test\\TableTest");

        if (!dirpath.exists())
        {
            dirpath.mkdirs();
        }

        PrintWriter writer = new PrintWriter("TableTest\\COMPUTER SCIENCE.txt", "UTF-8");
        String url = "http://appl101.lsu.edu/booklet2.nsf/f5e6e50d1d1d05c4862584410071cd2e?CreateDocument";
        
        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost("http://appl101.lsu.edu/booklet2.nsf/f5e6e50d1d1d05c4862584410071cd2e?CreateDocument");

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("%%Surrogate_SemesterDesc", "1"));
        params.add(new BasicNameValuePair("SemesterDesc", "Spring 2020"));
        params.add(new BasicNameValuePair("%%Surrogate_Department", "1"));
        params.add(new BasicNameValuePair("Department", "CURRICULUM & INSTRUCTION"));
        post.setEntity(new UrlEncodedFormEntity(params));

        HttpResponse response = client.execute(post);
        String coursesURL = response.getLastHeader("Location").getValue();
        
        try
        {
            Document doc = Jsoup.connect(coursesURL).get();
            Elements pretags = doc.select("pre");
            Element pretext = pretags.get(0);

            ArrayList<String> lines = new ArrayList<String>();
            String pre = pretext.text();
            String line = "";

            for (int i = 0; i < pre.length(); i++)
            {
                char c = pre.charAt(i);

                if (c == '/' || c == '\\')
                {
                    c = ' ';
                }

                if (c == '\n')
                {
                    if (line.length() > 0)
                    {
                        if (line.charAt(0) == '(' || (Character.isDigit(line.charAt(2))))
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

            for (String s : lines)
            {
                System.out.println(s);
            }
        }
        catch (IOException e)
        {
            System.out.println("Uh oh");
        }
    }
}