import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Connection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.Header;
//import org.apache.http.params.HttpParams;
//import org.apache.http.client.params.HttpClientParams;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.Map.Entry;

import java.net.URL;
import java.net.URLEncoder;
import java.net.URLConnection;
import java.net.HttpURLConnection;

import java.io.PrintWriter;

/**
 *
 * @author CChee1
 */
public class ApacheTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        CloseableHttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
        HttpPost post = new HttpPost("http://appl101.lsu.edu/booklet2.nsf/f5e6e50d1d1d05c4862584410071cd2e?CreateDocument");
        
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("%%Surrogate_SemesterDesc", "1"));
        params.add(new BasicNameValuePair("SemesterDesc", "Spring 2020"));
        params.add(new BasicNameValuePair("%%Surrogate_Department", "1"));
        params.add(new BasicNameValuePair("Department", "COMPUTER SCIENCE"));
        post.setEntity(new UrlEncodedFormEntity(params));
        
        HttpResponse response = client.execute(post);
        
        String coursesURL = response.getLastHeader("Location").getValue();

        Document doc = Jsoup.connect(coursesURL).get();
        PrintWriter writer = new PrintWriter("CourseInformation.txt", "UTF-8");

        Element table = doc.select("table").get(0);
        Elements rows = table.select("tr");

        for (int i = 1; i < rows.size(); i++)
        {
            Element row = rows.get(i);
            Elements cols = row.select("td");

            for (int j = 1; j < cols.size(); j++)
            {
                Element col = cols.get(j);
                writer.printf("%s ", col.text());
                //System.out.printf("%s ", col.text());
            }

            writer.println();
        }

        writer.close();

        //System.out.println(location);
        client.close();
    }
}