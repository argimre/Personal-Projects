import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WordCounter {

    public static void buildHeader(FileWriter fileWriter, String filepath) throws IOException {
        fileWriter.write("<html>");
        fileWriter.write("<head>");
        fileWriter.write("<title>addy</title>");
        fileWriter.write("</head>");
        fileWriter.write("<body>");
        fileWriter.write("<h2>" + "Words Counted in: " + filepath + "</h2>");
        fileWriter.write("<hr>");
        fileWriter.write("<table border=\"1\">");
        fileWriter.write("<tbody>");
        fileWriter.write("<tr>");
        fileWriter.write("<th>Words</th>");
        fileWriter.write("<th>Counts</th>");
        fileWriter.write("</tr>");
    }

    public static void buildBody(FileWriter fileWriter, Map<String, Integer> wordMap) throws IOException {
        ArrayList<String> words = new ArrayList<>(wordMap.keySet());
        Collections.sort(words, String.CASE_INSENSITIVE_ORDER);

        for (String word : words) {
            fileWriter.write("<tr>");
            fileWriter.write("<td>" + word + "</td>");
            fileWriter.write("<td>" + wordMap.get(word) + "</td>");
            fileWriter.write("</tr>\n");
        }
    }

    public static void buildFooter(FileWriter fileWriter) throws IOException {
        fileWriter.write("</tbody>");
        fileWriter.write("</table>");
        fileWriter.write("</body>");
        fileWriter.write("</html>");
    }

    public static void countWords(Map<String, Integer> wordMap, String filePath) {
        File file = new File(filePath);
        String line = "";

        if (!file.exists() || !file.isFile()) {
            System.out.println("Error with file");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine()) != null) {
                line = line.replace("-", " ");
                String[] temp = line.split("[^a-zA-Z'-]+");
                for (String word : temp) {
                    word = word.toLowerCase().trim();
                    if (!word.isEmpty()) {
                        wordMap.put(word, wordMap.getOrDefault(word, 0) + 1);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        Map<String, Integer> wordMap = new HashMap<>();

        System.out.print("Please enter the filepath to read from: ");
        String filePath = input.nextLine();
        countWords(wordMap, filePath);

        try (FileWriter writer = new FileWriter("Java-Projects\\WordCounter\\data/index.html")) {
            buildHeader(writer, filePath);
            buildBody(writer, wordMap);
            buildFooter(writer);
        } catch (Exception e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
        input.close();
    }
}
