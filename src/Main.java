import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        //CountingRunner.makeCalculations(br);
        FilesParser filesParser = new FilesParser();
        ArrayList<Double> time = filesParser.get_timeList();
        Map<Double, ArrayList<Double>> expFile = new HashMap<>();
        ArrayList<Map<Double, ArrayList<Double>>> theorFiles = new ArrayList<>();

        try {
            expFile = filesParser.trimStringsAndGetData(0,
                    filesParser.getDirectories()[0],
                    filesParser.get_expFiles()[2]);
            List<String> fileList = filesParser.get_theorFileList();
            for (int i = 0; i < fileList.size(); i++) {
                theorFiles.add(filesParser.trimStringsAndGetData(1,
                        filesParser.getDirectories()[1],
                        filesParser.get_theorFileList().get(i)));
            }
            //System.out.println(expFile.get(0.00050));
            //System.out.println(expFile.get(-0.25));
            //System.out.println(expFile);
            //System.out.println(theorFiles.get(0.00050));
            //System.out.println(theorFiles);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*for (int i = 0; i < theorFiles.size(); i++) {
            Map<Double, ArrayList<Double>> theorFile = theorFiles.get(i);
            for (int ik = 0; ik < time.size(); ik++) {
                ArrayList<Double> theorFileLine = theorFile.get(time.get(ik));
                if (theorFileLine != null) {
                    for (int j = 0; j < theorFileLine.size(); j++) {
                        double theord = theorFileLine.get(j);
                        for (int k = 0; k < expFile.size(); k++) {
                            ArrayList<Double> expFileLine = expFile.get(time.get(ik));
                            if (expFileLine != null) {
                                for (int l = 0; l < expFileLine.size(); l++) {
                                    double expd = expFileLine.get(l);

                                    double div = expd / theord;
                                    if (div == 1) System.out.println(i);
                                }
                            }
                        }
                    }
                }
            }
        }*/

    }


}
