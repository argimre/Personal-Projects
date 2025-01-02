import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class TextGlossary {

    public static void buildHeader(FileWriter fileWriter) throws IOException {
        fileWriter.write("<html>\n");
        fileWriter.write("<head>\n");
        fileWriter.write("<title>Glossary</title>\n");
        fileWriter.write("</head>\n");
        fileWriter.write("<body>\n");
        fileWriter.write("<h2>Glossary</h2>\n");
        fileWriter.write("<hr>\n");
        fileWriter.write("<h3>Index</h3>\n");
        fileWriter.write("<ul>\n");
    }

    public static void buildBody(Map<String, String> wordMap, FileWriter indexWriter, String targetDir)
            throws IOException {
        List<String> terms = new ArrayList<>(wordMap.keySet());
        Collections.sort(terms, String.CASE_INSENSITIVE_ORDER);

        for (String term : terms) {
            String termFileName = term.toLowerCase().replace(" ", "_") + ".html";
            String termFilePath = targetDir + File.separator + termFileName;

            indexWriter.write("<li><a href=\"" + termFileName + "\">" + term + "</a></li>\n");
            try (FileWriter termFileWriter = new FileWriter(termFilePath)) {
                termFileWriter.write("<html>\n");
                termFileWriter.write("<head><title>" + term + "</title></head>\n");
                termFileWriter.write("<body>\n");
                termFileWriter.write("<h2><b><i><font color=\"red\">" + term + "</font></i></b></h2>\n");

                String definition = processDefinition(terms, wordMap.get(term));
                termFileWriter.write(definition + "\n");

                termFileWriter.write("</blockquote>\n");
                termFileWriter.write("<hr>\n");
                termFileWriter.write("<p>Return to <a href=\"index.html\">index</a>.</p>\n");
                termFileWriter.write("</body>\n");
                termFileWriter.write("</html>\n");
            }
        }
    }

    public static String processDefinition(List<String> terms, String definition) {
        for (String term : terms) {
            String termFile = term.toLowerCase().replace(" ", "_") + ".html";
            definition = definition.replaceAll("\\b" + term + "\\b",
                    "<a href=\"" + termFile + "\">" + term + "</a>");
        }
        return definition;
    }

    public static void buildFooter(FileWriter fileWriter) throws IOException {
        fileWriter.write("</ul>\n");
        fileWriter.write("</body>\n");
        fileWriter.write("</html>\n");
    }

    public static void storeWords(Map<String, String> wordMap, String filePath) {
        String term = "";
        String line = "";
        StringBuilder definition = new StringBuilder();
        File file = new File(filePath);

        if (!file.isFile() || !file.exists()) {
            System.err.println("Error with file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) {
                    if (!term.isEmpty() && definition.length() > 0) {
                        wordMap.put(term.trim(), definition.toString());
                        term = "";
                        definition.setLength(0);
                    }
                } else {
                    if (term.isEmpty()) {
                        term = line;
                    } else {
                        definition.append(line).append(" ");
                    }
                }
            }
            if (!term.isEmpty() && definition.length() > 0) {
                wordMap.put(term.trim(), definition.toString().trim());
            }

        } catch (Exception e) {
            System.err.println("Error reading from file: " + e.getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner input = new Scanner(System.in);
        Map<String, String> wordMap = new HashMap<>();
        String targetDir = "Java-Projects\\TextGlossary\\data";

        System.out.print("Please enter the filepath to read from: ");
        String filePath = input.nextLine();
        storeWords(wordMap, filePath);

        String indexPath = targetDir + File.separator + "index.html";
        try (FileWriter writer = new FileWriter(indexPath)) {
            buildHeader(writer);
            buildBody(wordMap, writer, targetDir);
            buildFooter(writer);
        } catch (Exception e) {
            System.err.println("Error writing to file: " + e.getLocalizedMessage());
        }
        System.out.println("Glossary files have been generated successfully in " + targetDir);
        input.close();
    }
}
