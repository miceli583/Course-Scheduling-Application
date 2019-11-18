import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.List;
import java.util.Map.Entry;

import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;
import java.net.HttpURLConnection;

/**
 *
 * @author CChee1
 */
public class PostTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        String url = "http://appl101.lsu.edu/booklet2.nsf/f5e6e50d1d1d05c4862584410071cd2e?CreateDocument";
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        String semesterDesc = "Spring 2020";
        String department = "AEROSPACE STUDIES";
        String params = String.format("%%%%Surrogate_SemesterDesc=1&SemesterDesc=%s&%%%%Surrogate_Department=1&Department=%s", semesterDesc, department);
        byte[] postData = params.getBytes(charset);

        // Open connection
        URLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoOutput(true);
        connection.setRequestProperty("User-Agent", "Java client");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.write(postData);
        wr.flush();
        wr.close();

        /*BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String contentType = connection.getContentType().toString();
        System.out.printf("Content-Type: %s", contentType);*/

        /*try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream())))
        {
            for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet())
            {
                System.out.println(header.getKey() + "=" + header.getValue());
            }
        }*/

        /*for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet())
        {
            System.out.println(header.getKey() + "=" + header.getValue());
        }*/

        StringBuilder content;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream())))
        {
            String line;
            content = new StringBuilder();

            while ((line = br.readLine()) != null)
            {
                content.append(line);
                content.append(System.lineSeparator());
            }
        }

        System.out.println(content.toString());
        
        /*String query = String.format("SemesterDesc=%s&Department=%s",
            URLEncoder.encode(semesterDesc, charset),
            URLEncoder.encode(department, charset));

        URLConnection connection = new URL(url).openConnection();
        connection.setDoOutput(true); // Triggers POST
        connection.setRequestProperty("Accept-Charset", charset);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + charset);

        try (OutputStream output = connection.getOutputStream())
        {
            output.write(query.getBytes(charset));
        }

        InputStream response = connection.getInputStream();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(response, charset)))
        {
            for (String line; (line = reader.readLine()) != null;)
            {
                System.out.println(line);
            }
        }*/
    }  
}