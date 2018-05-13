import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

class TracesCounter implements Runnable {

    private static final double tmin = -0.25f, tmax = 1.25f;
    private static final int Nsensors = 10, Nsteps = 1000;
    private static final double[] sensor = {0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9};
    private static final double Tc = 1.0, Tmix = 0.2;
    private String _fileName;
    private double _omega;
    private double _theta0;

    TracesCounter(String fileName, double omega, double theta0) {
        _fileName = fileName;
        _omega = omega;
        _theta0 = theta0;
    }

    ArrayList<Double> count(String fileName, double omega, double theta0) {
        double time, delta, theta, step;
        ArrayList<Double> T = new ArrayList<>();

        DecimalFormat dFormat = new DecimalFormat("####0.00000");
        DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
        sym.setDecimalSeparator('.');
        dFormat.setDecimalFormatSymbols(sym);

        step = (tmax - tmin) / Nsteps;

        List<String> lines = new ArrayList<>();
        lines.add(" time     " + "    T01     " + "    T02     " + "    T03     " + "    T04     " +
                "    T05     " + "    T06     " + "    T07     " + "    T08     " + "    T09     " +
                "    T10     ");

        // Opening or creating a file
        new File("calculationResults/").mkdir();
        Path file = Paths.get("calculationResults/" + fileName);

        for (int j = 0; j <= Nsteps; j++) {
            time = tmin + (step * j);
            delta = shift(time);
            theta = theta0 + (omega * time);
            for (int k = 0; k < Nsensors; k++) {
                T.add(k, T_model(sensor[k], theta, delta, Tmix, Tc));
            }
            if (0 > time) {
                lines.add("" + dFormat.format(time) + "     " + dFormat.format(T.get(0)) + "     " +
                        dFormat.format(T.get(1)) + "     " + dFormat.format(T.get(2)) + "     " +
                        dFormat.format(T.get(3)) + "     " + dFormat.format(T.get(4)) + "     " +
                        dFormat.format(T.get(5)) + "     " + dFormat.format(T.get(6)) + "     " +
                        dFormat.format(T.get(7)) + "     " + dFormat.format(T.get(8)) + "     " +
                        dFormat.format(T.get(9)));
            } else {
                lines.add(" " + dFormat.format(time) + "     " + dFormat.format(T.get(0)) + "     " +
                        dFormat.format(T.get(1)) + "     " + dFormat.format(T.get(2)) + "     " +
                        dFormat.format(T.get(3)) + "     " + dFormat.format(T.get(4)) + "     " +
                        dFormat.format(T.get(5)) + "     " + dFormat.format(T.get(6)) + "     " +
                        dFormat.format(T.get(7)) + "     " + dFormat.format(T.get(8)) + "     " +
                        dFormat.format(T.get(9)));
            }
            // Writing to file
            try {
                Files.write(file, lines, Charset.forName("UTF-8"));
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        }
        return T;
    }

    private static double T_model (double r, double theta, double delta, double Tmin, double Tmax) {
        double T_model;
        double xi, eta, r_sq, rho_sq, d, r1, r2;

        xi = r * Math.cos(theta);
        eta = r * Math.sin(theta);
        r_sq = r * r;
        rho_sq = (xi + delta) * (xi + delta) + eta * eta;
        d = Math.sqrt(2 - delta * delta);
        r1 = (d - delta) * 0.5f;
        r2 = (d + delta) * 0.5f;

        if (r_sq > (r2 * r2)) {
            T_model = Tmax + (Tmin - Tmax) * r_sq;
        } else if (rho_sq < (r1 * r1)) {
            T_model = Tmax + (Tmin - Tmax) * rho_sq;
        } else {
            T_model = (Tmax + Tmin) * 0.5f;
        }
        return T_model;
    }

    private double shift (double time) {
        double shift;
        if (0.0 >= time) {
            shift = 0.0;
        } else if (1.0 <= time) {
            shift = 1.0;
        } else {
            shift = time;
        }
        return shift;
    }

    @Override
    public void run() {
        count(_fileName,_omega,_theta0);
        System.out.println("omega = " + _omega + " theta0 = " +  _theta0);
    }
}
