/******************************************************************************
Contributors:
Matthew Miceli
Christopher Chee
Shaun McFadden
Kareem Abdo 
*******************************************************************************/

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Node;
import org.jsoup.Connection;

import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

public class SemDepListBuilder {

    /* This method connects to the LSU course scheduling website and retrieves lists of the
        semesters, years, and departments. Then it creates 2 text files: Semesters.txt and
        Departments.txt. Semesters.txt contains all the semesters and years. Departments.txt
        contains a list of all the departments courses are offered in. The text files are
        stored in the folder SemDepLists. */
    public static void main(String[] args) throws IOException {
        
        File dirpath = new File("SemDepLists");

        if (!dirpath.exists())
        {
            dirpath.mkdirs();
        }

        PrintWriter writer = new PrintWriter("SemDepLists\\Semesters.txt", "UTF-8");
        
        // Establishes connection to LSU course scheduling website
        String url = "http://appl101.lsu.edu/booklet2.nsf/mainframeset";
        
        // Parses document and writes data into text files
        Document main = Jsoup.connect(url).get();
        Element frame = main.select("frame").get(0);
        Document selectFrame = Jsoup.connect(frame.absUrl("src")).get();
        Elements options = selectFrame.getElementsByAttributeValue("name", "SemesterDesc").get(0).children();

        for (Element option : options)
        {
            writer.printf("%s\n", option.text());
        }

        writer.close();
        writer = new PrintWriter("SemDepLists\\Departments.txt", "UTF-8");

        options = selectFrame.getElementsByAttributeValue("name", "Department").get(0).children();
        
        for (Element option : options)
        {
            writer.printf("%s\n", option.text());
        }

        writer.close();
    }
}