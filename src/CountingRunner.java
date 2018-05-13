import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class CountingRunner {
    private static String _name = "default_file_name";
    private static double _omega = 0, _theta0 = 0;
    private static final double pi = Math.PI;
    private static final double pi2 = pi*2;
    private static ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    CountingRunner() {}

    private static void readFileName(@NotNull BufferedReader br) {
        try {
            System.out.println("Enter name of file (File will have .dat extension. If file exists, it will be overwritten): ");
            String name = br.readLine();
            if ((name != null) && !name.trim().isEmpty()) {
                _name = name /*+ ".dat"*/;
            } else {
                readFileName(br);
            }
        } catch (IOException ioe) {
            System.out.println("Couldn't read line ");
            System.out.println();
            readFileName(br);
        }
    }

    private static void readOmega(@NotNull BufferedReader br) {
        try {
            System.out.print("Enter Omega: ");
            _omega = Float.parseFloat(br.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid Format!");
            System.out.println();
            readOmega(br);
        } catch (IOException ioe) {
            System.out.println("Couldn't read line");
            System.out.println();
            readOmega(br);
        }
    }

    private static void readTheta0(@NotNull BufferedReader br) {
        try {
            System.out.print("Enter Theta0: ");
            _theta0 = Float.parseFloat(br.readLine());
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid Format!");
            System.out.println();
            readTheta0(br);
        } catch (IOException ioe) {
            System.out.println("Couldn't read line");
            System.out.println();
            readTheta0(br);
        }
    }

    static void makeCalculations(BufferedReader br) {
        try {
            System.out.println("-----------------------------------------------");

            readFileName(br);
            //readOmega(br);
            //readTheta0(br);

            System.out.println("-----------------------------------------------");
            System.out.println("Calculating...");

            for (int counter = 0; counter < 41; counter++) {
                for (int innerCounter = 0; innerCounter < 7; innerCounter++) {
                    executorService.execute(new TracesCounter(_name + "_omega" + counter + "theta" +
                            innerCounter + ".dat", counter, innerCounter));
                }
                executorService.execute(new TracesCounter(_name + "_omega" + counter + "thetapi" +
                        ".dat", counter, pi));
                executorService.execute(new TracesCounter(_name + "_omega" + counter + "thetapi2" +
                        ".dat", counter, pi2));
            }

            System.out.println("(Type Enter key to exit the program when the program will stop counting)");
            br.readLine();
        } catch (Exception e) {
            System.out.println("Something went wrong!");
            System.out.println();
            System.out.println(e.getMessage());
        } finally {
            try {
                br.close();
                executorService.shutdown();
                System.out.println("Counting finished");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
