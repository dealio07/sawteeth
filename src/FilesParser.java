import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class FilesParser {
    private final double tmin = -0.25, tmax = 1.25;
    private String[] _expFiles = {"all.dat", "all_03.dat", "all_06.dat"};
    private String[] directories = {"", "calculationResults/"};
    private ArrayList<String> _theorFileList = new ArrayList<>();
    private ArrayList<Double> _timeList = new ArrayList<>();

    FilesParser() {
        getTheorFilesNames();
    }

    private void getTheorFilesNames() {
        File folder = new File("calculationResults");
        File[] listOfFiles = folder.listFiles();

        assert listOfFiles != null;
        ArrayList<String> fileList = new ArrayList<>();
        if (listOfFiles.length > 0) {
            for (int i = 0; i < listOfFiles.length; i++) {
                if (listOfFiles[i].isFile()) {
                    fileList.add(listOfFiles[i].getName());
                }
            }
        } else System.out.println("Folder is empty");
        _theorFileList = fileList;
    }

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        return stringBuilder.toString();
    }

    /**
     *
     * @param theorOrExp (0 - experiment, 1 - theory)
     * @param directory ("" - experiment, "calculationResults/" - theory)
     * @param fileName
     * @return
     * @throws IOException
     */
    Map<Double, ArrayList<Double>> trimStringsAndGetData(int theorOrExp, String directory, String fileName) throws IOException {
        int colNum = (theorOrExp == 1) ? 11:97;
        Map<Double, ArrayList<Double>> resultDictionary = new HashMap<>();
        ArrayList<String> trimmedLines = new ArrayList<>();
        ArrayList<Double> doublesList = new ArrayList<>();
        ArrayList<Double> time = new ArrayList<>();
        String inputFile = readFile(String.format("%s%s", directory, fileName)).trim();
        String[] splittedFile = inputFile.split(" ");

        for (int i = 0; i < splittedFile.length; i++) {
            if (splittedFile[i].length() > 0) {
                String[] splitted = splittedFile[i].trim().split(System.getProperty("line.separator"));
                for (int j = 0; j < splitted.length; j++) {
                    String trimmed = splitted[j].trim();
                    if (!trimmed.equals("")) trimmedLines.add(trimmed);
                }
            }
        }
        for (int i = colNum; i < trimmedLines.size(); i++) {
            String line = trimmedLines.get(i);
            String [] lines = line.split("e");
            for (String l : lines) {
                if (!l.equals("-")) doublesList.add(Double.valueOf(l));
            }
        }
        for (int i = 0; i < doublesList.size(); i++) {
            if (i % colNum == 0) time.add(doublesList.get(i));
        }

        ArrayList<ArrayList<Double>> partitions = new ArrayList<>();

        for (int i = 0; i < doublesList.size(); i += colNum) {
            ArrayList<Double> partition = new ArrayList<>(doublesList.subList(i + 1, Math.min(i + colNum, doublesList.size())));
            partitions.add(partition);
        }
        if (theorOrExp == 0) {
            ArrayList<Double> normTime = normalizeTime(time);
            for (int i = 0; i < normTime.size()-1; i++) {
                resultDictionary.put(normTime.get(i), partitions.get(i));
            }
        } else {
            for (int i = 0; i < time.size(); i++) {
                resultDictionary.put(time.get(i), partitions.get(i));
            }
        }
        if (theorOrExp == 0) {
            return normalizeExpData(resultDictionary);
        } else {
            return resultDictionary;
        }
        //return resultDictionary;
    }

    Map<Double, ArrayList<Double>> normalizeExpData(Map<Double, ArrayList<Double>> expData) {
        Map<Double, ArrayList<Double>> normalizedData = new HashMap<>();
        ArrayList<Double> klist = new ArrayList<>(expData.keySet());
        Collections.sort(klist);
        double[] keys = new double[expData.size()];
        ArrayList<Double> keysList = new ArrayList<>();
        DecimalFormat dFormat = new DecimalFormat("####0.00000");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        dFormat.setDecimalFormatSymbols(sym);
        for (int i = 0; i < expData.size(); i++) {
            keys[i] = klist.get(i);
            keysList.add(keys[i]);
            _timeList.add(keys[i]);
        }
        ArrayList<Double> normTime = new ArrayList<>(normalizeTime(keysList));
        double[] allValues = new double[expData.size()*10];
        double firstFiveValuesSumm = 0;
        int valuesCount = 0;
        for (int i = 0; i < 5; i++) {
            ArrayList<Double> line = expData.get(keys[i]);

            allValues[valuesCount] = line.get(0);
            valuesCount++;
        }
        for (int i = 0; i < 5; i++) {
            firstFiveValuesSumm += allValues[i];
        }
        double normalizer = firstFiveValuesSumm / valuesCount;
        for (int i = 0; i < expData.size(); i++) {
            ArrayList<Double> line = expData.get(keys[i]);
            ArrayList<Double> normalizedLine = new ArrayList<>();
            for (int j = 0; j < line.size(); j++) {
                double t = line.get(j);
                t = t / normalizer;
                double tF = Double.parseDouble(dFormat.format(t));
                normalizedLine.add(j, tF);
            }
            normalizedData.put(normTime.get(i), normalizedLine);
        }
        return normalizedData;
    }

    ArrayList<Double> normalizeTime(ArrayList<Double> oldTime) {
        final double step = (tmax - tmin) / 1000;
        ArrayList<Double> newTime = new ArrayList<>();
        DecimalFormat dFormat = new DecimalFormat("####0.00000");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        dFormat.setDecimalFormatSymbols(sym);
        for (int j = 0; j <= 1000 - (1000 - oldTime.size()); j++) {
            double time = (tmin + (step * j));
            double t = Double.parseDouble(dFormat.format(time));
            newTime.add(t);
        }
        return newTime;
    }

    public String[] get_expFiles() {
        return _expFiles;
    }

    public String[] getDirectories() {
        return directories;
    }

    public List<String> get_theorFileList() {
        return _theorFileList;
    }

    public ArrayList<Double> get_timeList() {
        return _timeList;
    }
}
