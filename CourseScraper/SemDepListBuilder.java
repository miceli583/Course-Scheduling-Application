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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        File dirpath = new File("SemDepLists");

        if (!dirpath.exists())
        {
            dirpath.mkdirs();
        }

        PrintWriter writer = new PrintWriter("SemDepLists\\Semesters.txt", "UTF-8");
        String url = "http://appl101.lsu.edu/booklet2.nsf/mainframeset";
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