import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //CountingRunner.makeCalculations(br);
        FilesParser filesParser = new FilesParser();
        try {
            Map<Double, ArrayList<Double>> expFile = filesParser.trimStringsAndGetData(0,
                    filesParser.getDirectories()[0],
                    filesParser.get_expFiles()[1]);
            Map<Double, ArrayList<Double>> theorFile = filesParser.trimStringsAndGetData(1,
                    filesParser.getDirectories()[1],
                    filesParser.get_theorFileList().get(10));
            System.out.println(expFile.get(60.14385));
            System.out.println(theorFile.get(0.00050));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
