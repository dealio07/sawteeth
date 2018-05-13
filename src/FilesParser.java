import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Dictionary;

public class FilesParser {
    private static String[] expFiles = {"all", "all_03", "all_06"};
    static Dictionary<String, String> expDatStrings;
    static String theorDatString;

    FilesParser() {
    }

    static void getExpDatFilesStrings() {
        for (String fileName : expFiles) {
            try {
                expDatStrings.put(fileName, readFile(fileName));
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    static void getTheorDatFileString(String fileName) {
        try {
            theorDatString = readFile(fileName);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    static void getTheorFilesNames() {
        File folder = new File("calculationResults");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                System.out.println("File " + listOfFiles[i].getName());
            } else if (listOfFiles[i].isDirectory()) {
                System.out.println("Directory " + listOfFiles[i].getName());
            }
        }
    }

    private static String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = " ";

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
    }

    private static void trimStrings() {
        getExpDatFilesStrings();
        getTheorDatFileString(""); //TODO: add file name
    }
}
