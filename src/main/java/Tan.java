import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;

import static java.lang.Double.*;

@Data
@AllArgsConstructor
public class Tan {
    private final Sin sin;
    private final Cos cos;

    public Tan(){
        this.sin = new Sin();
        this.cos = new Cos();
    }

    public double tan(double x, double eps) {
        double ans = sin.sin(x,eps) / cos.cos(x,eps);
        if (isNaN(ans) || ans == 0) {
            return NaN;
        }
        return ans;
    }

    public double writeResultToCSV(double x, double eps, Writer out) {
        double res = tan(x, eps);
        try (CSVPrinter printer = CSVFormat.DEFAULT.print(out)) {
            printer.printRecord(x, res);
        } catch (IOException e) {
            System.out.println("Wrong filename");
        }
        return res;
    }
}
